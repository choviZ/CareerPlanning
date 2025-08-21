<template>
  <div class="user-login-container">
    <el-card class="login-card">
      <h2 class="login-title">用户登录</h2>
      <el-form ref="loginFormRef" :model="loginInfo" :rules="rules" label-width="80px"
               @submit.prevent="onFinish">
        <el-form-item label="账号" prop="userAccount">
          <el-input v-model="loginInfo.userAccount" placeholder="请输入账号" />
        </el-form-item>

        <el-form-item label="密码" prop="userPassword">
          <el-input type="password" v-model="loginInfo.userPassword" placeholder="请输入密码"
                    show-password />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" native-type="submit">登录</el-button>
          <router-link to="/user/register" style="margin-left: 10px;">注册</router-link>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { userLogin } from '@/api/userController'
import { ElMessage, type FormRules } from 'element-plus'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

// 登录信息
const loginInfo = reactive<API.UserLoginRequest>({
  userAccount: '',
  userPassword: ''
})

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
  const res = await userLogin(loginInfo)
  if (res.data.code === 200 && res.data.data) {
    await userStore.fetchLoginUser() // 登录成功-获取用户态
    ElMessage.success('登录成功')
    await router.push('/')
  } else {
    ElMessage.error(res.data.message || '登录失败，请检查账号或密码')
  }
}
</script>

<style scoped>
.user-login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 64px);
  /* Adjust based on header height */
  background-color: #f0f2f5;
}

.login-card {
  width: 400px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  padding: 20px;
}

.login-title {
  text-align: center;
  margin-bottom: 24px;
  color: #333;
}
</style>
