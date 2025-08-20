import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Home',
      component: () => import('@/views/Home.vue')
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
    }
  ]
})

export default router
