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
      component: () => import('@/views/user/UserLogin.vue'),
    },
    {
      path: '/user/register',
      name: 'UserRegister',
      component: () => import('@/views/user/UserRegister.vue'),
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
    },
    {
      path: '/management/result-career-mapping',
      name: 'ResultCareerMappingManagement',
      component: () => import('@/views/management/ResultCareerMappingManagement.vue')
    },
    {
      path: '/management/assessment-result',
      name: 'AssessmentResultManagement',
      component: () => import('@/views/management/AssessmentResultManagement.vue')
    },
    {
      path: '/management/resume-template',
      name: 'ResumeTemplateManagement',
      component: () => import('@/views/management/ResumeTemplateManagement.vue')
    },
    {
      path: '/resume/templates',
      name: 'ResumeTemplateGallery',
      component: () => import('@/views/resume/ResumeTemplateGallery.vue')
    },
    {
      path: '/management/user',
      name: 'UserManagement',
      component: () => import('@/views/management/UserManagement.vue')
    },
    {
      path: '/management/comment',
      name: 'CommentManagement',
      component: () => import('@/views/management/CommentManagement.vue')
    },
    {
      path: '/management/post',
      name: 'PostManagement',
      component: () => import('@/views/management/PostManagement.vue')
    },
    {
      path: '/resume/edit',
      name: 'ResumeEditor',
      component: () => import('@/views/resume/ResumeEditor.vue')
    },
    {
      path: '/community',
      name: 'Community',
      component: () => import('@/views/community/CommunityPage.vue')
    },
    {
      path: '/community/post/:id',
      name: 'PostDetail',
      component: () => import('@/views/community/PostDetail.vue')
    }
  ]
})

export default router
