<template>
  <div class="order-summary">
    <h2>訂單確認</h2>

    <div v-if="!items || items.length === 0">
      購物車為空。請先加入商品。
    </div>

    <div v-else>
      <ul>
        <li v-for="it in items" :key="it.productId">
          {{ it.productName }} × {{ it.buyQty }} = {{ formatCurrency(it.price * it.buyQty) }}
        </li>
      </ul>

      <p><strong>訂單總金額：</strong> {{ formatCurrency(total) }}</p>

      <div class="controls">
        <label>
          會員編號：
          <input v-model="memberId" placeholder="輸入 memberId（測試用）" />
        </label>

        <button @click="submitOrder" :disabled="submitting || items.length === 0 || hasInvalid">
          {{ submitting ? '建立中...' : '建立訂單' }}
        </button>

        <button @click="$emit('cancel')" :disabled="submitting">取消</button>
      </div>

      <p v-if="hasInvalid" class="error">訂單含不合法數量；請回購物車修正。</p>

      <div v-if="error" class="error">錯誤：{{ error }}</div>
      <div v-if="successOrderId" class="success">建立成功！訂單編號：{{ successOrderId }}</div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import axios from 'axios'

const props = defineProps({
  items: {
    type: Array,
    required: true,
    default: () => []
  },
  total: {
    type: Number,
    required: true,
    default: 0
  }
})

const emit = defineEmits(['order-created', 'cancel'])

// local state
const memberId = ref('M001') // 測試用，實際可由登入系統提供
const submitting = ref(false)
const error = ref(null)
const successOrderId = ref(null)

// 資料驗證（同 Cart）
const hasInvalid = computed(() => {
  return props.items.some((it) => !Number.isFinite(it.buyQty) || it.buyQty < 1 || it.buyQty > it.quantity)
})

watch(() => props.items, () => {
  // 若 items 改變，清除之前錯誤
  error.value = null
  successOrderId.value = null
})

// format
function formatCurrency(v) {
  const n = Number(v) || 0
  return n.toLocaleString() + ' 元'
}

// 送出訂單（呼叫後端 API）
// 範例 payload:
// {
//   memberId: 'M001',
//   items: [{ productId: 'P001', quantity: 1 }, ...]
// }
async function submitOrder() {
  if (props.items.length === 0) return
  if (hasInvalid.value) {
    error.value = '訂單含不合法數量'
    return
  }

  error.value = null
  successOrderId.value = null
  submitting.value = true

  const payload = {
    memberId: memberId.value,
    items: props.items.map((it) => ({ productId: it.productId, quantity: it.buyQty }))
  }

  try {
    // NOTE:
    // 後端 API 範例： POST /api/orders
    // 回傳 JSON 範例： { orderId: "Ms202601181234567890", totalAmount: 1234 }
    const res = await axios.post('http://localhost:8080/api/orders', payload, { timeout: 10000 })
    // 假設後端回傳 orderId
    const orderId = res.data?.orderId ?? res.data?.order_id ?? null
    successOrderId.value = orderId || '（後端未回傳 orderId）'

    // emit event 給父元件：告知建立完成（父可以清空購物車）
    emit('order-created', { orderId: successOrderId.value, payload })
  } catch (e) {
    console.error(e)
    if (e.response?.data) {
      // 後端回傳錯誤訊息
      error.value = e.response.data.message || JSON.stringify(e.response.data)
    } else {
      error.value = e.message || '無法連線到後端'
    }
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.order-summary { max-width: 700px; margin: 12px auto; }
.controls { margin-top: 12px; display:flex; gap:12px; align-items:center; }
.controls input { padding: 6px; }
.error { color: #c00; margin-top: 8px; }
.success { color: #080; margin-top: 8px; }
</style>
