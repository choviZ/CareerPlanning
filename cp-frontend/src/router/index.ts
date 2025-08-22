import { createRouter, createWebHistory } from 'vue-router'
import BasicLayout from '@/layouts/BasicLayout.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Home',
      component: () => import('@/views/Home.vue')
    },
    {
      path: '/user/login',
      name: 'UserLogin',
      component: () => import('@/views/UserLogin.vue'),
    },
    {
      path: '/user/register',
      name: 'UserRegister',
      component: () => import('@/views/UserRegister.vue'),
    },
    {
      path: '/assessment',
      name: 'Assessment',
      component: () => import('@/views/assessment/AssessmentEntry.vue')
    },
    {
      path: '/assessmentPage',
      name: 'AssessmentPage',
      component: () => import('@/views/assessment/AssessmentPage.vue')
    },
    {
      path: '/management/career',
      name: 'CareerManagement',
      component: () => import('@/views/management/CareerManagement.vue')
    }
  ]
})

export default router
