<template>
  <div class="user-management">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>用户管理</span>
          <el-button type="primary" @click="handleAdd">新增用户</el-button>
        </div>
      </template>

      <!-- 搜索区域 -->
      <el-form :inline="true" :model="searchParams" class="search-form">
        <el-form-item label="用户账号">
          <el-input v-model="searchParams.userAccount" placeholder="请输入用户账号" clearable
                    style="width: 180px;"></el-input>
        </el-form-item>
        <el-form-item label="用户名称">
          <el-input v-model="searchParams.userName" placeholder="请输入用户名称" clearable
                    style="width: 180px;"></el-input>
        </el-form-item>
        <el-form-item label="用户角色">
          <el-select v-model="searchParams.userRole" placeholder="请选择用户角色" clearable
                     style="width: 180px;">
            <el-option label="管理员" value="admin"></el-option>
            <el-option label="普通用户" value="user"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格区域 -->
      <el-table :data="tableData" v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="userAccount" label="用户账号" width="150"></el-table-column>
        <el-table-column prop="userName" label="用户名称" width="150"></el-table-column>
        <el-table-column prop="userAvatar" label="头像" width="80">
          <template #default="{ row }">
            <el-avatar v-if="row.userAvatar" :src="row.userAvatar" :size="40"></el-avatar>
            <el-avatar v-else :size="40">{{ row.userName?.charAt(0) || 'U' }}</el-avatar>
          </template>
        </el-table-column>
        <el-table-column prop="userProfile" label="用户简介"
                         show-overflow-tooltip></el-table-column>
        <el-table-column prop="userRole" label="用户角色" width="120">
          <template #default="{ row }">
            <el-tag :type="getRoleTagType(row.userRole)">{{ getRoleName(row.userRole) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" :icon="Edit" size="small" @click="handleEdit(row)">编辑
            </el-button>
            <el-button type="danger" :icon="Delete" size="small" @click="handleDelete(row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <el-pagination
        class="pagination"
        :current-page="pagination.currentPage"
        :page-size="pagination.pageSize"
        :total="pagination.total"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
        layout="total, sizes, prev, pager, next, jumper"
        :page-sizes="[10, 20, 50, 100]"
      />
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px"
               :close-on-click-modal="false">
      <el-form :model="form" :rules="formRules" ref="formRef" label-width="100px">
        <el-form-item label="用户账号" prop="userAccount">
          <el-input v-model="form.userAccount" placeholder="请输入用户账号"
                    :disabled="isEdit"></el-input>
        </el-form-item>
        <el-form-item label="用户密码" prop="userPassword" v-if="!isEdit">
          <el-input v-model="form.userPassword" type="password" placeholder="请输入用户密码"
                    show-password></el-input>
        </el-form-item>
        <el-form-item label="用户名称" prop="userName">
          <el-input v-model="form.userName" placeholder="请输入用户名称"></el-input>
        </el-form-item>
        <el-form-item label="头像链接" prop="userAvatar">
          <el-input v-model="form.userAvatar" placeholder="请输入头像链接"></el-input>
        </el-form-item>
        <el-form-item label="用户简介" prop="userProfile">
          <el-input v-model="form.userProfile" type="textarea" placeholder="请输入用户简介"
                    :rows="3"></el-input>
        </el-form-item>
        <el-form-item label="用户角色" prop="userRole">
          <el-select v-model="form.userRole" placeholder="请选择用户角色" style="width: 100%;">
            <el-option label="管理员" value="admin"></el-option>
            <el-option label="普通用户" value="user"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleConfirm" :loading="submitLoading">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Edit, Delete } from '@element-plus/icons-vue'
import type { FormInstance, FormRules } from 'element-plus'
import {
  listUserByPage,
  addUser,
  updateUser,
  deleteUser
} from '@/api/userController'

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const tableData = ref<API.UserVO[]>([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref<FormInstance>()

// 搜索参数
const searchParams = reactive<API.UserQueryRequest>({
  current: 1,
  pageSize: 10,
  userAccount: '',
  userName: '',
  userRole: ''
})

// 分页信息
const pagination = ref({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 表单数据
const form = reactive<API.UserAddRequest & API.UserUpdateRequest>({
  userAccount: '',
  userPassword: '',
  userName: '',
  userAvatar: '',
  userProfile: '',
  userRole: 'user'
})

// 表单验证规则
const formRules: FormRules = {
  userAccount: [
    { required: true, message: '请输入用户账号', trigger: 'blur' },
    { min: 4, max: 16, message: '用户账号长度为4-16位', trigger: 'blur' }
  ],
  userPassword: [
    { required: true, message: '请输入用户密码', trigger: 'blur' },
    { min: 8, max: 20, message: '用户密码长度为8-20位', trigger: 'blur' }
  ],
  userName: [
    { required: true, message: '请输入用户名称', trigger: 'blur' },
    { max: 50, message: '用户名称不能超过50个字符', trigger: 'blur' }
  ],
  userRole: [
    { required: true, message: '请选择用户角色', trigger: 'change' }
  ]
}

// 计算属性
const dialogTitle = ref('')

/**
 * 加载表格数据
 */
const loadTableData = async () => {
  loading.value = true
  try {
    const params = {
      ...searchParams,
      current: pagination.value.currentPage,
      pageSize: pagination.value.pageSize
    }
    const res = await listUserByPage(params)
    if (res.data.code === 200 && res.data.data) {
      tableData.value = res.data.data.records || []
      pagination.value.total = Number(res.data.data.totalRow) || 0
    } else {
      ElMessage.error(res.data.message || '获取用户列表失败')
    }
  } catch (error) {
    ElMessage.error('获取用户列表失败' + error)
  } finally {
    loading.value = false
  }
}

/**
 * 搜索
 */
const handleSearch = () => {
  pagination.value.currentPage = 1
  loadTableData()
}

/**
 * 重置搜索
 */
const handleReset = () => {
  searchParams.userAccount = ''
  searchParams.userName = ''
  searchParams.userRole = ''
  pagination.value.currentPage = 1
  loadTableData()
}

/**
 * 分页变化
 */
const handleCurrentChange = (page: number) => {
  pagination.value.currentPage = page
  loadTableData()
}

/**
 * 每页条数变化
 */
const handleSizeChange = (size: number) => {
  pagination.value.pageSize = size
  pagination.value.currentPage = 1
  loadTableData()
}

/**
 * 新增用户
 */
const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增用户'
  resetForm()
  dialogVisible.value = true
}

/**
 * 编辑用户
 */
const handleEdit = (row: API.UserVO) => {
  isEdit.value = true
  dialogTitle.value = '编辑用户'
  Object.assign(form, {
    id: row.id,
    userAccount: row.userAccount,
    userName: row.userName,
    userAvatar: row.userAvatar,
    userProfile: row.userProfile,
    userRole: row.userRole
  })
  dialogVisible.value = true
}

/**
 * 删除用户
 */
const handleDelete = async (row: API.UserVO) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除用户 "${row.userName}" 吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const res = await deleteUser({ id: row.id! })
    if (res.data.code === 200 && res.data.data) {
      ElMessage.success('删除成功')
      await loadTableData()
    } else {
      ElMessage.error(res.data.message || '删除失败')
    }
  } catch (error) {
    // 用户取消删除
    ElMessage.error('失败' + error)
  }
}

/**
 * 确认提交
 */
const handleConfirm = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    submitLoading.value = true

    let res
    if (isEdit.value) {
      res = await updateUser(form)
    } else {
      res = await addUser(form)
    }

    if (res.data.code === 200 && res.data.data) {
      ElMessage.success(isEdit.value ? '编辑成功' : '新增成功')
      dialogVisible.value = false
      await loadTableData()
    } else {
      ElMessage.error(res.data.message || '操作失败')
    }
  } catch (error) {
    // 表单验证失败
    ElMessage.error('失败' + error)
  } finally {
    submitLoading.value = false
  }
}

/**
 * 重置表单
 */
const resetForm = () => {
  Object.assign(form, {
    id: undefined,
    userAccount: '',
    userPassword: '',
    userName: '',
    userAvatar: '',
    userProfile: '',
    userRole: 'user'
  })
  formRef.value?.clearValidate()
}

/**
 * 获取角色名称
 */
const getRoleName = (role: string) => {
  const roleMap: Record<string, string> = {
    admin: '管理员',
    user: '普通用户'
  }
  return roleMap[role] || '未知'
}

/**
 * 获取角色标签类型
 */
const getRoleTagType = (role: string) => {
  const tagMap: Record<string, string> = {
    admin: 'danger',
    user: ''
  }
  return tagMap[role] || ''
}

// 页面加载时获取数据
onMounted(() => {
  loadTableData()
})
</script>

<style scoped>
.user-management {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
