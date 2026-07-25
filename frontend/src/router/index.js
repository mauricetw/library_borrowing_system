import { createRouter, createWebHistory } from 'vue-router'
import BookListView from '../views/BookListView.vue'
import LoginView from '../views/LoginView.vue'
import RecordView from '../views/RecordView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: BookListView,
      meta: { requiresAuth: true } // 需要登入才能看
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
    {
      path: '/records',
      name: 'records',
      component: RecordView,
      meta: { requiresAuth: true } // 需要登入才能看
    }
  ]
})

// 路由守衛 (Navigation Guard)
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token');
  
  // 如果這個頁面需要登入，但是沒有 token
  if (to.meta.requiresAuth && !token) {
    next('/login'); // 強制導向登入頁
  } else if (to.path === '/login' && token) {
    // 如果已經登入了還想去登入頁，就踢回首頁
    next('/');
  } else {
    next(); // 正常放行
  }
})

export default router
