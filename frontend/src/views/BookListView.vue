<template>
  <div>
    <h2>書籍與庫存列表</h2>
    <div v-if="loading" class="loading">載入中...</div>
    <div v-else class="book-grid">
      <div v-for="book in books" :key="book.isbn" class="book-card">
        <div class="book-info">
          <h3>{{ book.name }}</h3>
          <p><strong>作者：</strong>{{ book.author }}</p>
          <p><strong>ISBN：</strong>{{ book.isbn }}</p>
          <p class="intro">{{ book.introduction }}</p>
        </div>
        <div class="inventory-section">
          <h4>庫存狀態：</h4>
          <ul>
            <li v-for="inv in book.inventories" :key="inv.inventoryId">
              {{ inv.inventoryId }} - 
              <span :class="{'status-avail': inv.status === '在庫', 'status-unavail': inv.status !== '在庫'}">
                {{ inv.status }}
              </span>
              <button 
                v-if="inv.status === '在庫'" 
                @click="borrowBook(inv.inventoryId)"
                class="borrow-btn"
                :disabled="actionLoading === inv.inventoryId">
                {{ actionLoading === inv.inventoryId ? '處理中...' : '借閱' }}
              </button>
            </li>
          </ul>
          <p v-if="!book.inventories || book.inventories.length === 0">目前無庫存資料</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from '@/utils/axios'

const books = ref([])
const loading = ref(true)
const actionLoading = ref(null)

const fetchBooks = async () => {
  try {
    const response = await axios.get('/api/books')
    books.value = response.data
  } catch (error) {
    console.error('取得書籍失敗', error)
    alert('取得書籍列表失敗')
  } finally {
    loading.value = false
  }
}

const borrowBook = async (inventoryId) => {
  if (!confirm(`確定要借閱編號 ${inventoryId} 嗎？`)) return
  
  actionLoading.value = inventoryId
  try {
    await axios.post('/api/borrow', { inventoryId })
    alert('借閱成功！')
    await fetchBooks() // 重新整理列表以更新庫存狀態
  } catch (error) {
    alert(error.response?.data?.message || '借閱失敗')
  } finally {
    actionLoading.value = null
  }
}

onMounted(() => {
  fetchBooks()
})
</script>

<style scoped>
.book-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 2rem;
}
.book-card {
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 1.5rem;
  background: #fff;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}
.book-info h3 { margin-top: 0; color: #2c3e50; }
.intro { color: #666; font-size: 0.9rem; line-height: 1.4; }
.inventory-section {
  margin-top: 1rem;
  padding-top: 1rem;
  border-top: 1px solid #eee;
}
ul { list-style: none; padding: 0; margin: 0; }
li {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.5rem 0;
  border-bottom: 1px dashed #eee;
}
.status-avail { color: #28a745; font-weight: bold; }
.status-unavail { color: #dc3545; font-weight: bold; }
.borrow-btn {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 0.3rem 0.8rem;
  border-radius: 4px;
  cursor: pointer;
}
.borrow-btn:disabled { background-color: #ccc; }
.loading { text-align: center; font-size: 1.2rem; margin-top: 2rem; }
</style>