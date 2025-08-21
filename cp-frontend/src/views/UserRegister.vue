<template>
  <div class="user-register-container">
    <el-card class="register-card">
      <h2 class="register-title">用户注册</h2>
      <el-form ref="registerFormRef" :model="registerInfo" :rules="rules" label-width="80px"
               @submit.prevent="onFinish">
        <el-form-item label="账号" prop="userAccount">
          <el-input v-model="registerInfo.userAccount" placeholder="请输入账号" />
        </el-form-item>

        <el-form-item label="密码" prop="userPassword">
          <el-input type="password" v-model="registerInfo.userPassword" placeholder="请输入密码"
                    show-password />
        </el-form-item>

        <el-form-item label="确认密码" prop="checkPassword">
          <el-input type="password" v-model="registerInfo.checkPassword"
                    placeholder="请再次输入密码" show-password />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" native-type="submit">注册</el-button>
          <router-link to="/user/login" style="margin-left: 10px;">已有账号？去登录</router-link>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { userRegister } from '@/api/userController'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import type { FormInstance, FormRules } from 'element-plus'

// 注册信息
const registerInfo = reactive<API.UserRegisterRequest>({
  userAccount: '',
  userPassword: '',
  checkPassword: ''
})

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
      const res = await userRegister(registerInfo as API.UserRegisterRequest)
      if (res.data.code === 200 && res.data.data) {
        ElMessage.success('注册成功')
        await router.push('/user/login')
      } else {
        ElMessage.error(res.data.message || '注册失败')
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
  min-height: calc(100vh - 64px);
  /* Adjust based on header height */
  background-color: #f0f2f5;
}

.register-card {
  width: 400px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  padding: 20px;
}

.register-title {
  text-align: center;
  margin-bottom: 24px;
  color: #333;
}
</style>
