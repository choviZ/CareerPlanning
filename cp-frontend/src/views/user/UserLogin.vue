<template>
  <div class="user-login-container">
    <div class="login-wrapper">
      <!-- 头部Logo和应用名称 -->
      <div class="login-header">
        <div class="logo-container">
          <div class="logo-icon">
            <el-icon><UserFilled /></el-icon>
          </div>
        </div>
        <h1 class="app-name">职业规划平台</h1>
      </div>
      
      <!-- 登录卡片 -->
      <el-card class="login-card" :body-style="{ padding: '30px' }">
        <h2 class="login-title">欢迎登录</h2>
        <p class="login-subtitle">请输入您的账号和密码</p>
        
        <el-form 
          ref="loginFormRef"
          :model="loginInfo" 
          :rules="rules" 
          label-position="top"
          @submit.prevent="onFinish"
          class="login-form"
        >
          <el-form-item label="账号" prop="userAccount">
            <el-input 
              v-model="loginInfo.userAccount" 
              placeholder="请输入账号" 
              :prefix-icon="User"
              size="large"
            />
          </el-form-item>
          
          <el-form-item label="密码" prop="userPassword">
            <el-input 
              type="password" 
              v-model="loginInfo.userPassword" 
              placeholder="请输入密码" 
              :prefix-icon="Lock"
              size="large"
              show-password
            />
          </el-form-item>
          
          <div class="form-actions">
            <el-checkbox v-model="rememberMe">记住我</el-checkbox>
            <a href="#" class="forgot-password">忘记密码?</a>
          </div>
          
          <el-form-item class="login-button-container">
            <el-button type="primary" native-type="submit" class="login-button" :loading="loading">
              {{ loading ? '登录中...' : '登录' }}
            </el-button>
          </el-form-item>
          
          <div class="register-link-container">
            <span>还没有账号?</span>
            <router-link to="/user/register" class="register-link">立即注册</router-link>
          </div>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { userLogin } from '@/api/userController.ts'
import { ElMessage, type FormRules } from 'element-plus'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user.ts'
import { User, Lock, UserFilled } from '@element-plus/icons-vue'

// 登录信息
const loginInfo = reactive<API.UserLoginRequest>({
  userAccount: '',
  userPassword: ''
})

// 记住我选项
const rememberMe = ref(false)

// 加载状态
const loading = ref(false)

// 表单验证规则
const rules = reactive<FormRules>({
  userAccount: [
    { required: true, message: '请输入账号!', trigger: 'blur' }
  ],
  userPassword: [
    { required: true, message: '请输入密码!', trigger: 'blur' }
  ]
})

const router = useRouter()
const userStore = useUserStore()

/**
 * 登录事件
 */
const onFinish = async () => {
  if (!loginInfo) return
  loading.value = true
  try {
    const res = await userLogin(loginInfo)
    if (res.data.code === 200 && res.data.data) {
      await userStore.fetchLoginUser() // 登录成功-获取用户态
      ElMessage.success('登录成功')
      await router.push('/')
    } else {
      ElMessage.error(res.data.message || '登录失败，请检查账号或密码')
    }
  } catch (error) {
    console.error('登录失败:', error)
    ElMessage.error('登录失败，请稍后再试')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.user-login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  width: 100vw;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 0;
  margin: 0;
  box-sizing: border-box;
  overflow-x: hidden;
}

.login-wrapper {
  width: 100%;
  max-width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  animation: fadeIn 0.8s ease-in-out;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-20px); }
  to { opacity: 1; transform: translateY(0); }
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
  color: white;
}

.logo-container {
  display: flex;
  justify-content: center;
  margin-bottom: 15px;
}

.logo-icon {
  width: 70px;
  height: 70px;
  background-color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

.logo-icon .el-icon {
  font-size: 36px;
  color: #764ba2;
}

.app-name {
  font-size: 28px;
  font-weight: 700;
  margin: 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.login-card {
  width: 100%;
  max-width: 460px;
  border-radius: 16px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
  background-color: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border: none;
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.login-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.25);
}

.login-title {
  text-align: center;
  margin-bottom: 10px;
  color: #333;
  font-size: 24px;
  font-weight: 600;
}

.login-subtitle {
  text-align: center;
  margin-bottom: 30px;
  color: #666;
  font-size: 14px;
}

.login-form {
  margin-top: 20px;
}

.login-form :deep(.el-form-item__label) {
  font-weight: 500;
  color: #333;
  padding-bottom: 5px;
}

.login-form :deep(.el-input__wrapper) {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  border-radius: 8px;
  transition: all 0.3s ease;
}

.login-form :deep(.el-input__wrapper:hover) {
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.login-form :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #764ba2 inset;
}

.form-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.forgot-password {
  color: #764ba2;
  text-decoration: none;
  font-size: 14px;
  transition: color 0.3s ease;
}

.forgot-password:hover {
  color: #667eea;
  text-decoration: underline;
}

.login-button-container {
  margin-bottom: 15px;
}

.login-button {
  width: 100%;
  height: 44px;
  font-size: 16px;
  font-weight: 500;
  background: linear-gradient(to right, #667eea, #764ba2);
  border: none;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.login-button:hover {
  background: linear-gradient(to right, #5a6eea, #6a3a9c);
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(118, 75, 162, 0.4);
}

.login-button:active {
  transform: translateY(0);
}

.register-link-container {
  text-align: center;
  margin-top: 20px;
  color: #666;
  font-size: 14px;
}

.register-link {
  color: #764ba2;
  font-weight: 500;
  text-decoration: none;
  margin-left: 5px;
  transition: color 0.3s ease;
}

.register-link:hover {
  color: #667eea;
  text-decoration: underline;
}

/* 响应式设计 */
@media (max-width: 480px) {
  .login-wrapper {
    max-width: 100%;
  }
  
  .login-card {
    border-radius: 12px;
  }
  
  .app-name {
    font-size: 24px;
  }
  
  .logo-icon {
    width: 60px;
    height: 60px;
  }
  
  .logo-icon .el-icon {
    font-size: 30px;
  }
}
</style>
