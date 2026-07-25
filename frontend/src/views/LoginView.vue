<template>
  <div class="login-container">
    <div class="login-card">
      <h2>{{ isRegister ? '註冊帳號' : '會員登入' }}</h2>
      <form @submit.prevent="handleSubmit">
        <div class="form-group" v-if="isRegister">
          <label>使用者名稱</label>
          <input type="text" v-model="form.userName" required />
        </div>
        <div class="form-group">
          <label>手機號碼</label>
          <input type="text" v-model="form.phoneNumber" required />
        </div>
        <div class="form-group">
          <label>密碼</label>
          <input type="password" v-model="form.password" required />
        </div>
        <button type="submit" class="submit-btn" :disabled="loading">
          {{ loading ? '處理中...' : (isRegister ? '註冊' : '登入') }}
        </button>
      </form>
      <p class="toggle-text">
        {{ isRegister ? '已經有帳號了？' : '還沒有帳號？' }}
        <a href="#" @click.prevent="isRegister = !isRegister">
          {{ isRegister ? '點此登入' : '點此註冊' }}
        </a>
      </p>
      <p v-if="errorMessage" class="error-msg">{{ errorMessage }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from '@/utils/axios'

const router = useRouter()
const isRegister = ref(false)
const loading = ref(false)
const errorMessage = ref('')

const form = ref({
  userName: '',
  phoneNumber: '',
  password: ''
})

const handleSubmit = async () => {
  loading.value = true
  errorMessage.value = ''
  try {
    if (isRegister.value) {
      await axios.post('/api/auth/register', {
        userName: form.value.userName,
        phoneNumber: form.value.phoneNumber,
        password: form.value.password
      })
      alert('註冊成功！請重新登入。')
      isRegister.value = false
    } else {
      const response = await axios.post('/api/auth/login', {
        phoneNumber: form.value.phoneNumber,
        password: form.value.password
      })
      localStorage.setItem('token', response.data.token)
      router.push('/')
    }
  } catch (error) {
    errorMessage.value = error.response?.data?.message || '發生錯誤，請檢查帳號密碼'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 80vh;
}
.login-card {
  width: 100%;
  max-width: 400px;
  padding: 2rem;
  box-shadow: 0 4px 6px rgba(0,0,0,0.1);
  border-radius: 8px;
  background: white;
}
h2 { text-align: center; margin-bottom: 1.5rem; }
.form-group { margin-bottom: 1rem; }
label { display: block; margin-bottom: 0.5rem; font-weight: bold; }
input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}
.submit-btn {
  width: 100%;
  padding: 0.75rem;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  margin-top: 1rem;
}
.submit-btn:disabled { background-color: #9ea7a3; }
.toggle-text { text-align: center; margin-top: 1rem; }
.toggle-text a { color: #007bff; text-decoration: none; }
.error-msg { color: #dc3545; text-align: center; margin-top: 1rem; font-weight: bold; }
</style>