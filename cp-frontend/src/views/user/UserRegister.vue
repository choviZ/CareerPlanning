<template>
  <div class="user-register-container">
    <div class="register-wrapper">
      <!-- 头部Logo和应用名称 -->
      <div class="register-header">
        <div class="logo-container">
          <div class="logo-icon">
            <el-icon><UserFilled /></el-icon>
          </div>
        </div>
        <h1 class="app-name">职业规划平台</h1>
      </div>
      
      <!-- 注册卡片 -->
      <el-card class="register-card" :body-style="{ padding: '30px' }">
        <h2 class="register-title">创建账号</h2>
        <p class="register-subtitle">填写以下信息完成注册</p>
        
        <el-form 
          ref="registerFormRef" 
          :model="registerInfo" 
          :rules="rules" 
          label-position="top"
          @submit.prevent="onFinish"
          class="register-form"
        >
          <el-form-item label="账号" prop="userAccount">
            <el-input 
              v-model="registerInfo.userAccount" 
              placeholder="请输入账号" 
              :prefix-icon="User"
              size="large"
            />
          </el-form-item>
          
          <el-form-item label="密码" prop="userPassword">
            <el-input 
              type="password" 
              v-model="registerInfo.userPassword" 
              placeholder="请输入密码"
              :prefix-icon="Lock"
              size="large"
              show-password 
            />
          </el-form-item>
          
          <el-form-item label="确认密码" prop="checkPassword">
            <el-input 
              type="password" 
              v-model="registerInfo.checkPassword"
              placeholder="请再次输入密码" 
              :prefix-icon="Lock"
              size="large"
              show-password 
            />
          </el-form-item>
          
          <div class="register-button-container">
            <el-button 
              type="primary" 
              native-type="submit" 
              class="register-button"
              :loading="loading"
            >
              {{ loading ? '注册中...' : '注册账号' }}
            </el-button>
          </div>
        </el-form>
        
        <div class="login-link-container">
          已有账号？<router-link to="/user/login" class="login-link">立即登录</router-link>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { userRegister } from '@/api/userController.ts'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import type { FormInstance, FormRules } from 'element-plus'
import { User, Lock, UserFilled } from '@element-plus/icons-vue'

// 注册信息
const registerInfo = reactive<API.UserRegisterRequest>({
  userAccount: '',
  userPassword: '',
  checkPassword: ''
})

// 加载状态
const loading = ref(false)

const registerFormRef = ref<FormInstance>()

const validatePass = (rule: any, value: string, callback: any) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== registerInfo.userPassword) {
    callback(new Error('两次输入的密码不一致!'))
  } else {
    callback()
  }
}

// 表单验证规则
const rules = reactive<FormRules>({
  userAccount: [
    { required: true, message: '请输入账号!', trigger: 'blur' }
  ],
  userPassword: [
    { required: true, message: '请输入密码!', trigger: 'blur' }
  ],
  checkPassword: [
    { required: true, message: '请再次输入密码!', trigger: 'blur' },
    { validator: validatePass, trigger: 'blur' }
  ]
})

const router = useRouter()

/**
 * 注册
 */
const onFinish = async () => {
  if (!registerFormRef.value) return
  
  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const res = await userRegister(registerInfo as API.UserRegisterRequest)
        if (res.data.code === 200 && res.data.data) {
          ElMessage.success('注册成功')
          await router.push('/user/login')
        } else {
          ElMessage.error(res.data.message || '注册失败')
        }
      } catch (error) {
        console.error('注册失败:', error)
        ElMessage.error('注册失败，请稍后再试')
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.user-register-container {
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

.register-wrapper {
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

.register-header {
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

.register-card {
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

.register-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.25);
}

.register-title {
  text-align: center;
  margin-bottom: 10px;
  color: #333;
  font-size: 24px;
  font-weight: 600;
}

.register-subtitle {
  text-align: center;
  margin-bottom: 30px;
  color: #666;
  font-size: 14px;
}

.register-form {
  margin-top: 20px;
}

.register-form :deep(.el-form-item__label) {
  font-weight: 500;
  color: #333;
  padding-bottom: 5px;
}

.register-form :deep(.el-input__wrapper) {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  border-radius: 8px;
  transition: all 0.3s ease;
}

.register-form :deep(.el-input__wrapper:hover) {
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.register-form :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #764ba2 inset;
}

.register-button-container {
  margin: 20px 0 15px;
}

.register-button {
  width: 100%;
  height: 44px;
  font-size: 16px;
  font-weight: 500;
  background: linear-gradient(to right, #667eea, #764ba2);
  border: none;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.register-button:hover {
  background: linear-gradient(to right, #5a6eea, #6a3a9c);
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(118, 75, 162, 0.4);
}

.register-button:active {
  transform: translateY(0);
}

.login-link-container {
  text-align: center;
  margin-top: 20px;
  color: #666;
  font-size: 14px;
}

.login-link {
  color: #764ba2;
  font-weight: 500;
  text-decoration: none;
  margin-left: 5px;
  transition: color 0.3s ease;
}

.login-link:hover {
  color: #667eea;
  text-decoration: underline;
}

/* 响应式设计 */
@media (max-width: 480px) {
  .register-wrapper {
    max-width: 100%;
  }
  
  .register-card {
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
