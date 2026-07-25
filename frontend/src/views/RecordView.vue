<template>
  <div>
    <h2>我的借閱紀錄</h2>
    <div v-if="loading" class="loading">載入中...</div>
    <div v-else>
      <table v-if="records.length > 0" class="record-table">
        <thead>
          <tr>
            <th>借閱編號</th>
            <th>書籍名稱</th>
            <th>庫存 ID</th>
            <th>借出時間</th>
            <th>歸還時間</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="record in records" :key="record.recordId">
            <td>{{ record.recordId }}</td>
            <td>{{ record.inventory?.book?.name || '未知書籍' }}</td>
            <td>{{ record.inventory?.inventoryId || '未知庫存' }}</td>
            <td>{{ formatDate(record.borrowingTime) }}</td>
            <td>{{ record.returnTime ? formatDate(record.returnTime) : '尚未歸還' }}</td>
            <td>
              <button 
                v-if="!record.returnTime" 
                @click="returnBook(record.inventory.inventoryId)"
                class="return-btn"
                :disabled="actionLoading === record.inventory.inventoryId">
                {{ actionLoading === record.inventory.inventoryId ? '處理中...' : '歸還' }}
              </button>
              <span v-else class="returned-text">已歸還</span>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-else class="no-data">目前沒有任何借閱紀錄。</div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from '@/utils/axios'

const records = ref([])
const loading = ref(true)
const actionLoading = ref(null)

const fetchRecords = async () => {
  try {
    const response = await axios.get('/api/records')
    records.value = response.data
  } catch (error) {
    console.error('取得紀錄失敗', error)
    alert('取得紀錄失敗')
  } finally {
    loading.value = false
  }
}

const returnBook = async (inventoryId) => {
  if (!confirm('確定要歸還這本書嗎？')) return
  
  actionLoading.value = inventoryId
  try {
    await axios.post('/api/return', { inventoryId })
    alert('歸還成功！')
    await fetchRecords() // 重新整理紀錄以更新還書時間
  } catch (error) {
    alert(error.response?.data?.message || '歸還失敗')
  } finally {
    actionLoading.value = null
  }
}

const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString('zh-TW', {
    year: 'numeric', month: '2-digit', day: '2-digit',
    hour: '2-digit', minute: '2-digit', second: '2-digit'
  })
}

onMounted(() => {
  fetchRecords()
})
</script>

<style scoped>
.record-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
  background: #fff;
  box-shadow: 0 1px 3px rgba(0,0,0,0.1);
}
th, td {
  padding: 1rem;
  text-align: left;
  border-bottom: 1px solid #eee;
}
th { background-color: #f8f9fa; font-weight: bold; color: #333; }
.return-btn {
  background-color: #28a745;
  color: white;
  border: none;
  padding: 0.4rem 1rem;
  border-radius: 4px;
  cursor: pointer;
}
.return-btn:disabled { background-color: #ccc; }
.returned-text { color: #6c757d; font-weight: bold; }
.loading, .no-data { text-align: center; font-size: 1.2rem; margin-top: 2rem; color: #666; }
</style>