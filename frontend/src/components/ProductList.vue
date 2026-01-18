<template>
  <div>
    <h2>商品清單</h2>
    <ul>
      <li v-for="p in products" :key="p.productId">
        {{ p.productName }} - {{ p.price }} 元 (庫存 {{ p.quantity }})
      </li>
    </ul>
  </div>
</template>

<script setup>
import axios from 'axios'
import { ref, onMounted } from 'vue'

const products = ref([])

onMounted(async () => {
  const res = await axios.get('http://localhost:8080/api/products')
  products.value = res.data
})
</script>
