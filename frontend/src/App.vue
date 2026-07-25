<template>
  <div id="app">
    <nav v-if="isLoggedIn" class="navbar">
      <div class="nav-brand">圖書借閱系統</div>
      <div class="nav-links">
        <router-link to="/">書籍列表</router-link>
        <router-link to="/records">我的借閱紀錄</router-link>
        <button @click="logout" class="logout-btn">登出</button>
      </div>
    </nav>
    <main class="container">
      <router-view></router-view>
    </main>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

// 簡單判斷是否登入 (檢查 localStorage 是否有 token)
const isLoggedIn = computed(() => {
  return !!localStorage.getItem('token') && route.path !== '/login'
})

const logout = () => {
  localStorage.removeItem('token')
  router.push('/login')
}
</script>

<style scoped>
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 2rem;
  background-color: #f8f9fa;
  border-bottom: 1px solid #dee2e6;
  margin-bottom: 2rem;
}
.nav-brand {
  font-size: 1.5rem;
  font-weight: bold;
  color: #2c3e50;
}
.nav-links a {
  margin-right: 1.5rem;
  text-decoration: none;
  color: #42b983;
  font-weight: bold;
}
.nav-links a.router-link-active {
  color: #2c3e50;
}
.logout-btn {
  padding: 0.5rem 1rem;
  background-color: #dc3545;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1rem;
}
</style>
