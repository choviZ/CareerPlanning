<template>
  <div class="career-management">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>职业管理</span>
          <el-button type="primary" @click="handleAdd">新增职业</el-button>
        </div>
      </template>

      <!-- 搜索区域 -->
      <el-form :inline="true" :model="searchParams" class="search-form">
        <el-form-item label="职业名称">
          <el-input v-model="searchParams.careerName" placeholder="请输入职业名称"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格区域 -->
      <el-table :data="tableData" v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="ID" width="180"></el-table-column>
        <el-table-column prop="name" label="职业名称" width="180"></el-table-column>
        <el-table-column prop="description" label="描述"></el-table-column>
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
        layout="total, prev, pager, next, jumper"
      ></el-pagination>
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="职业名称">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="所需技能">
          <el-input v-model="form.requiredSkills" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="就业前景">
          <el-input v-model="form.jobOutlook" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="平均薪资">
          <el-input v-model="form.averageSalary" type="textarea"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleConfirm">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Edit, Delete } from '@element-plus/icons-vue'
import { addCareer, deleteCareer, listCareerByPage, updateCareer } from '@/api/careerController.ts'

// 搜索参数
const searchParams = ref({
  careerName: ''
})

// 表格数据
const tableData = ref<API.CareerVo[]>([])
const loading = ref(false)

// 分页
const pagination = ref({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 弹窗
const dialogVisible = ref(false)
const dialogTitle = ref('')
const form = ref<API.CareerUpdateRequest>({
  id: '',
  name: '',
  description: '',
  requiredSkills: '',
  jobOutlook: '',
  averageSalary: ''
})

// 加载表格数据
const loadTableData = async () => {
  loading.value = true
  try {
    const res = await listCareerByPage({
      ...searchParams.value,
      current: pagination.value.currentPage,
      pageSize: pagination.value.pageSize
    })
    if (res.data.code === 200 && res.data.data) {
      tableData.value = res.data.data.records
      pagination.value.total = Number(res.data.data.totalRow)
    } else {
      ElMessage.error(res.data.message || '获取数据失败')
    }
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = async () => {
  pagination.value.currentPage = 1
  const res = await listCareerByPage({
    name: searchParams.value.careerName
  })
  if (res.data.code === 200 && res.data.data) {
    tableData.value = res.data.data.records
    pagination.value.total = Number(res.data.data.totalRow)
  }
}

// 重置
const handleReset = () => {
  searchParams.value = {
    careerName: ''
  }
  pagination.value.currentPage = 1
  loadTableData()
}

// 分页变化
const handleCurrentChange = (page: number) => {
  pagination.value.currentPage = page
  loadTableData()
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增职业'
  form.value = {
    id: undefined,
    name: '',
    description: ''
  }
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row: API.Career) => {
  dialogTitle.value = '编辑职业'
  form.value = { ...row }
  dialogVisible.value = true
}

// 删除
const handleDelete = (row: API.Career) => {
  ElMessageBox.confirm(`确认删除职业【${row.name}】吗？`, '提示', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    const res = await deleteCareer({ id: row.id })
    if (res.data.code === 200) {
      ElMessage.success('删除成功')
      await loadTableData()
    } else {
      ElMessage.error(res.data.message || '删除失败')
    }
  })
}

// 弹窗确认
const handleConfirm = async () => {
  let res
  if (form.value.id) {
    // 编辑
    res = await updateCareer(form.value)
  } else {
    // 新增
    res = await addCareer(form.value)
  }
  if (res.data.code === 200) {
    ElMessage.success(form.value.id ? '编辑成功' : '新增成功')
    dialogVisible.value = false
    await loadTableData()
  } else {
    ElMessage.error(res.data.message || '操作失败')
  }
}

// 页面加载时获取数据
onMounted(() => {
  loadTableData()
})
</script>

<style scoped>
.career-management {
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
