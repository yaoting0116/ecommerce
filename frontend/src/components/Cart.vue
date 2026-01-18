<template>
  <div class="cart">
    <h2>購物車</h2>

    <div v-if="cart.length === 0">購物車目前沒有商品。</div>

    <table v-else class="cart-table">
      <thead>
        <tr>
          <th>商品名稱</th>
          <th>單價</th>
          <th>數量</th>
          <th>小計</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(item, idx) in cart" :key="item.productId">
          <td class="name">{{ item.productName }}</td>
          <td class="price">{{ formatCurrency(item.price) }}</td>
          <td class="qty">
            <button @click="decrease(idx)" :disabled="item.buyQty <= 1">-</button>
            <input
              type="number"
              :min="1"
              :max="item.quantity"
              v-model.number="item.buyQty"
              @change="onQtyChange(idx)"
            />
            <button @click="increase(idx)" :disabled="item.buyQty >= item.quantity">+</button>
            <div class="stock-note">（庫存 {{ item.quantity }}）</div>
          </td>
          <td class="sub">{{ formatCurrency(item.price * item.buyQty) }}</td>
          <td class="ops">
            <button @click="removeItem(idx)">刪除</button>
          </td>
        </tr>
      </tbody>
    </table>

    <div class="summary" v-if="cart.length > 0">
      <p><strong>訂單總金額：</strong> {{ formatCurrency(totalPrice) }}</p>
      <div class="actions">
        <button @click="emitCheckout" :disabled="hasInvalidQuantity || loading">
          前往結帳
        </button>
        <button @click="clearCart" :disabled="loading">清空購物車</button>
      </div>
      <p v-if="hasInvalidQuantity" class="error">請修正數量（數量不得超過庫存且至少為 1）。</p>
    </div>

    <div v-if="loading" class="loading">處理中...</div>
  </div>
</template>

<script setup>
import { reactive, computed, toRefs, watch } from 'vue'

const props = defineProps({
  cartItems: {
    type: Array,
    required: true,
    default: () => []
  },
  loading: {
    // 父元件可選擇傳入 loading 狀態；若沒傳入，本元件內部也有 loading 控制（this example uses internal）
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update-cart', 'checkout', 'order-created'])

// 建立本地可變的 cart（避免直接修改 props）
const cart = reactive([])

// 當 props.cartItems 變動時同步到本地 cart（深複製）
watch(
  () => props.cartItems,
  (newVal) => {
    cart.splice(0) // 清空
    if (Array.isArray(newVal)) {
      newVal.forEach((it) => {
        // 確保 buyQty 欄位存在，且為數字
        cart.push({
          productId: it.productId,
          productName: it.productName,
          price: Number(it.price),
          quantity: Number(it.quantity),
          buyQty: Number(it.buyQty ?? 1)
        })
      })
    }
  },
  { immediate: true, deep: true }
)

// computed：總價
const totalPrice = computed(() =>
  cart.reduce((s, it) => s + (Number(it.price) || 0) * (Number(it.buyQty) || 0), 0)
)

// 是否有不合法數量（<=0 或 > 庫存）
const hasInvalidQuantity = computed(() =>
  cart.some((it) => !Number.isFinite(it.buyQty) || it.buyQty < 1 || it.buyQty > it.quantity)
)

// format currency
function formatCurrency(v) {
  const n = Number(v) || 0
  return n.toLocaleString() + ' 元'
}

// 按鈕方法
function increase(idx) {
  const it = cart[idx]
  if (it.buyQty < it.quantity) {
    it.buyQty = it.buyQty + 1
    emitUpdate()
  }
}
function decrease(idx) {
  const it = cart[idx]
  if (it.buyQty > 1) {
    it.buyQty = it.buyQty - 1
    emitUpdate()
  }
}
function onQtyChange(idx) {
  const it = cart[idx]
  if (!Number.isFinite(it.buyQty) || it.buyQty < 1) it.buyQty = 1
  if (it.buyQty > it.quantity) it.buyQty = it.quantity
  emitUpdate()
}
function removeItem(idx) {
  cart.splice(idx, 1)
  emitUpdate()
}
function clearCart() {
  cart.splice(0)
  emitUpdate()
}

// 當本地 cart 更新時，通知父元件（傳回一個深複本）
function emitUpdate() {
  const out = cart.map((it) => ({
    productId: it.productId,
    productName: it.productName,
    price: it.price,
    quantity: it.quantity,
    buyQty: it.buyQty
  }))
  emit('update-cart', out)
}

// 觸發結帳事件 -> 父元件或 OrderSummary 處理
function emitCheckout() {
  if (hasInvalidQuantity.value) return
  // 傳出 cart 與 total
  emit('checkout', {
    items: cart.map((it) => ({ productId: it.productId, quantity: it.buyQty })),
    rawItems: cart.map((it) => ({ ...it })),
    total: totalPrice.value
  })
}
</script>

<style scoped>
.cart { max-width: 900px; margin: 12px auto; }
.cart-table { width: 100%; border-collapse: collapse; margin-bottom: 8px; }
.cart-table th, .cart-table td { border: 1px solid #ddd; padding: 8px; text-align: left; vertical-align: middle; }
.cart-table .price, .cart-table .sub { width: 120px; }
.qty input { width: 68px; text-align: center; margin: 0 6px; }
.stock-note { font-size: 12px; color: #666; }
.actions { margin-top: 10px; }
.error { color: #c00; margin-top: 8px; }
.loading { color: #06c; margin-top: 8px; }
</style>
