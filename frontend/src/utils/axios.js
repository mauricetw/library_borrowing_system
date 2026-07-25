import axios from 'axios';

// 建立一個自訂的 axios 實體
const instance = axios.create({
  // 因為我們在 vite.config.js 已經設定了 proxy，所以這裡不需要寫 http://localhost:8080
  timeout: 5000
});

// Request 攔截器：在發送請求前執行
instance.interceptors.request.use(
  (config) => {
    // 從 localStorage 取得 token
    const token = localStorage.getItem('token');
    if (token) {
      // 如果有 token，就加到 Header 中
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default instance;