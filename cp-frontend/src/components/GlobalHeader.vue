<template>
  <el-row class="header-row">
    <!-- 左侧 Logo 和网站名称 -->
    <el-col :span="4" class="logo-col">
      <div class="logo">
        <span>CareerPlan</span>
      </div>
    </el-col>

    <!-- 中间菜单项 -->
    <el-col :span="16" class="menu-col">
      <el-menu mode="horizontal" class="menu-container" :router="true">
        <el-menu-item v-for="item in menuItems" :key="item.path" :index="item.path"
                      :route="{ path: item.path }">
          {{ item.label }}
        </el-menu-item>
      </el-menu>
    </el-col>

    <!-- 右侧登录按钮或用户头像 -->
    <el-col :span="4" class="login-col">
      <div v-if="!userStore.currentUser" class="header-right">
        <el-button type="primary" class="login-btn" @click="toLoginPage">登录</el-button>
      </div>
      <div v-else class="user-info">
        <el-dropdown trigger="click" @command="handleCommand">
          <span class="el-dropdown-link">
            <el-avatar :size="32"
                       :src="userStore.currentUser.userAvatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epict.png'" />
            <span
              class="username">{{ userStore.currentUser.userName || userStore.currentUser.userAccount
              }}</span>
            <el-icon class="el-icon--right"><arrow-down /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-col>
  </el-row>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ArrowDown } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { userLogout } from '@/api/userController'

const userStore = useUserStore()
const router = useRouter()

onMounted(() => {
  userStore.fetchLoginUser()
})

const menuItems = [
  { path: '/', label: '首页' },
  { path: '/assessment', label: '职业测评' },
  { path: '/resume/templates', label: '简历模板' },
  { path: '/learning', label: '学习资源' },
  { path: '/community', label: '交流' },
  { path: '/management/user', label: '用户管理' },
  { path: '/management/career', label: '职业管理' },
  { path: '/management/assessment-result', label: '评估结果管理' },
  { path: '/management/result-career-mapping', label: '测评结果映射' },
  { path: '/management/resume-template', label: '简历模板管理' },
  { path: '/management/comment', label: '评论管理' },
  { path: '/management/post', label: '帖子管理' }
]

const toLoginPage = () => {
  router.push('/user/login')
}

const handleCommand = async (command: string) => {
  if (command === 'logout') {
    await doLayout()
  }
}

/**
 * 退出登录
 */
const doLayout = async () => {
  const res = await userLogout()
  if (res.data.code === 200 && res.data.data) {
    ElMessage.success('退出成功')
    userStore.clearLoginState()
    await router.push('/')
  } else {
    ElMessage.error(res.data.message || '退出失败')
  }
}
</script>

<style scoped>
.header-row {
  width: 100%;
  display: flex;
  align-items: center;
  background-color: #fff;
  padding: 0 20px;
  box-shadow: 0 0px 2px rgba(0, 0, 0, 0.1);
}

.logo-col {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  min-width: 150px;
}

.logo {
  font-size: 1.5rem;
  font-weight: bold;
  color: #4096ff;
  padding: 10px 0;
}

.menu-col {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.menu-container {
  width: 100%;
  justify-content: center;
}

.login-col {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  min-width: 100px;
}

.login-btn {
  min-width: 80px;
}

.user-info {
  display: flex;
  align-items: center;
}

.el-dropdown-link {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.username {
  margin-left: 8px;
  font-weight: bold;
}
</style>
