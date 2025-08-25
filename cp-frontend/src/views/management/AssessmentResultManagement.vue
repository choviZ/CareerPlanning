<template>
  <div class="assessment-result-management">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>评估结果管理</span>
          <el-button type="primary" @click="handleAdd">新增结果</el-button>
        </div>
      </template>

      <!-- 搜索区域 -->
      <el-form :inline="true" :model="searchParams" class="search-form">
        <el-form-item label="测试类型">
          <el-select v-model="searchParams.testType" placeholder="请选择测试类型" clearable style="width: 180px;">
            <el-option label="MBTI" value="MBTI"></el-option>
            <el-option label="Holland" value="Holland"></el-option>
            <el-option label="BigFive" value="BigFive"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="结果代码">
          <el-input v-model="searchParams.resultCode" placeholder="请输入结果代码" clearable style="width: 180px;"></el-input>
        </el-form-item>
        <el-form-item label="结果名称">
          <el-input v-model="searchParams.resultName" placeholder="请输入结果名称" clearable style="width: 180px;"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格区域 -->
      <el-table :data="tableData" v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="testType" label="测试类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getTestTypeTagType(row.testType)">{{ row.testType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="resultCode" label="结果代码" width="120"></el-table-column>
        <el-table-column prop="resultName" label="结果名称" width="180"></el-table-column>
        <el-table-column prop="resultDesc" label="结果描述" show-overflow-tooltip></el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column prop="updatedAt" label="更新时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.updatedAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" :icon="Edit" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" :icon="Delete" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
        label-position="left"
      >
        <el-form-item label="测试类型" prop="testType">
          <el-select v-model="form.testType" placeholder="请选择测试类型" style="width: 100%;">
            <el-option label="MBTI" value="MBTI"></el-option>
            <el-option label="Holland" value="Holland"></el-option>
            <el-option label="BigFive" value="BigFive"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="结果代码" prop="resultCode">
          <el-input v-model="form.resultCode" placeholder="请输入结果代码" maxlength="20"></el-input>
        </el-form-item>
        <el-form-item label="结果名称" prop="resultName">
          <el-input v-model="form.resultName" placeholder="请输入结果名称" maxlength="50"></el-input>
        </el-form-item>
        <el-form-item label="结果描述" prop="resultDesc">
          <el-input
            v-model="form.resultDesc"
            type="textarea"
            :rows="4"
            placeholder="请输入结果描述"
            maxlength="500"
            show-word-limit
          ></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" :loading="submitLoading" @click="handleConfirm">
            <el-icon v-if="submitLoading"><Loading /></el-icon>
            <el-icon v-else><Check /></el-icon>
            确认
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Edit, Delete, Loading, Check } from '@element-plus/icons-vue'
import {
  addAssessmentResult,
  deleteAssessmentResult,
  listAssessmentResultByPage,
  updateAssessmentResult
} from '@/api/assessmentResultController'

/**
 * 评估结果管理页面
 * 功能包括：搜索、分页查询、新增、编辑、删除评估结果
 */

// ==================== 响应式数据定义 ====================

// 搜索参数
const searchParams = ref({
  testType: '',
  resultCode: '',
  resultName: ''
})

// 表格数据
const tableData = ref<API.AssessmentResultVo[]>([])
const loading = ref(false)

// 分页配置
const pagination = ref({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 弹窗相关
const dialogVisible = ref(false)
const dialogTitle = ref('')
const submitLoading = ref(false)
const formRef = ref()

// 表单数据
const form = ref({
  id: undefined as number | undefined,
  testType: '',
  resultCode: '',
  resultName: '',
  resultDesc: ''
})

// 表单验证规则
const rules = reactive({
  testType: [{ required: true, message: '请选择测试类型', trigger: 'change' }],
  resultCode: [
    { required: true, message: '请输入结果代码', trigger: 'blur' },
    { min: 1, max: 20, message: '结果代码长度在 1 到 20 个字符', trigger: 'blur' }
  ],
  resultName: [
    { required: true, message: '请输入结果名称', trigger: 'blur' },
    { min: 1, max: 50, message: '结果名称长度在 1 到 50 个字符', trigger: 'blur' }
  ],
  resultDesc: [
    { required: true, message: '请输入结果描述', trigger: 'blur' },
    { min: 1, max: 500, message: '结果描述长度在 1 到 500 个字符', trigger: 'blur' }
  ]
})

// ==================== 工具函数 ====================

/**
 * 根据测试类型获取标签样式
 * @param testType 测试类型
 * @returns 标签类型
 */
const getTestTypeTagType = (testType: string) => {
  const typeMap: Record<string, string> = {
    'MBTI': 'primary',
    'Holland': 'success',
    'BigFive': 'warning'
  }
  return typeMap[testType] || 'info'
}

/**
 * 格式化日期时间
 * @param dateTime 日期时间字符串
 * @returns 格式化后的日期时间
 */
const formatDateTime = (dateTime: string | undefined) => {
  if (!dateTime) return '-'
  return new Date(dateTime).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

// ==================== 数据加载 ====================

/**
 * 加载表格数据
 */
const loadTableData = async () => {
  loading.value = true
  try {
    const params: API.QueryAssessmentResultRequest = {
      current: pagination.value.currentPage,
      pageSize: pagination.value.pageSize
    }

    // 添加搜索条件
    if (searchParams.value.testType) {
      params.testType = searchParams.value.testType
    }
    if (searchParams.value.resultCode) {
      params.resultCode = searchParams.value.resultCode
    }
    if (searchParams.value.resultName) {
      params.resultName = searchParams.value.resultName
    }

    const res = await listAssessmentResultByPage(params)
    if (res.data.code === 200 && res.data.data) {
      tableData.value = res.data.data.records || []
      pagination.value.total = Number(res.data.data.totalRow) || 0
    } else {
      ElMessage.error(res.data.message || '获取数据失败')
    }
  } catch (error) {
    console.error('加载数据失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// ==================== 搜索功能 ====================

/**
 * 处理搜索
 */
const handleSearch = () => {
  pagination.value.currentPage = 1
  loadTableData()
}

/**
 * 重置搜索条件
 */
const handleReset = () => {
  searchParams.value = {
    testType: '',
    resultCode: '',
    resultName: ''
  }
  pagination.value.currentPage = 1
  loadTableData()
}

// ==================== 分页功能 ====================

/**
 * 处理页码变化
 * @param page 新页码
 */
const handleCurrentChange = (page: number) => {
  pagination.value.currentPage = page
  loadTableData()
}

/**
 * 处理每页条数变化
 * @param size 新的每页条数
 */
const handleSizeChange = (size: number) => {
  pagination.value.pageSize = size
  pagination.value.currentPage = 1
  loadTableData()
}

// ==================== 增删改功能 ====================

/**
 * 处理新增
 */
const handleAdd = () => {
  dialogTitle.value = '新增评估结果'
  dialogVisible.value = true
  form.value = {
    id: undefined,
    testType: '',
    resultCode: '',
    resultName: '',
    resultDesc: ''
  }
}

/**
 * 处理编辑
 * @param row 行数据
 */
const handleEdit = (row: API.AssessmentResultVo) => {
  dialogTitle.value = '编辑评估结果'
  dialogVisible.value = true
  form.value = {
    id: row.id,
    testType: row.testType || '',
    resultCode: row.resultCode || '',
    resultName: row.resultName || '',
    resultDesc: row.resultDesc || ''
  }
}

/**
 * 处理删除
 * @param row 行数据
 */
const handleDelete = (row: API.AssessmentResultVo) => {
  ElMessageBox.confirm(
    `确认删除评估结果【${row.testType} - ${row.resultCode} - ${row.resultName}】吗？`,
    '提示',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const res = await deleteAssessmentResult({ id: row.id! })
      if (res.data.code === 200) {
        ElMessage.success('删除成功')
        await loadTableData()
      } else {
        ElMessage.error(res.data.message || '删除失败')
      }
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  })
}

/**
 * 处理弹窗确认
 */
const handleConfirm = async () => {
  if (!formRef.value) return

  try {
    // 表单验证
    await formRef.value.validate()

    submitLoading.value = true

    let res
    if (form.value.id) {
      // 编辑模式
      const updateData: API.UpdateAssessmentResultRequest = {
        id: form.value.id,
        testType: form.value.testType,
        resultCode: form.value.resultCode,
        resultName: form.value.resultName,
        resultDesc: form.value.resultDesc
      }
      res = await updateAssessmentResult(updateData)
    } else {
      // 新增模式
      const addData: API.AddAssessmentResultRequest = {
        testType: form.value.testType,
        resultCode: form.value.resultCode,
        resultName: form.value.resultName,
        resultDesc: form.value.resultDesc
      }
      res = await addAssessmentResult(addData)
    }

    if (res.data.code === 200) {
      ElMessage.success(form.value.id ? '编辑成功' : '新增成功')
      dialogVisible.value = false
      await loadTableData()
    } else {
      ElMessage.error(res.data.message || '操作失败')
    }
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('提交失败')
  } finally {
    submitLoading.value = false
  }
}

// ==================== 生命周期 ====================

/**
 * 页面加载时获取数据
 */
onMounted(() => {
  loadTableData()
})
</script>

<style scoped>
/* 页面容器样式 */
.assessment-result-management {
  padding: 20px;
}

/* 卡片头部样式 */
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* 搜索表单样式 */
.search-form {
  margin-bottom: 20px;
}

/* 确保选择器placeholder样式正确显示 */
.search-form .el-select {
  min-width: 180px;
}

.search-form .el-select .el-input__inner::placeholder,
.search-form .el-select .el-input__wrapper input::placeholder {
  color: #a8abb2 !important;
  font-style: normal;
  opacity: 1;
}

.search-form .el-select .el-input__wrapper {
  box-shadow: 0 0 0 1px #dcdfe6 inset;
}

.search-form .el-select .el-input__wrapper:hover {
  box-shadow: 0 0 0 1px #c0c4cc inset;
}

/* 确保placeholder文字不被截断 */
.search-form .el-select .el-input {
  width: 100%;
}

.search-form .el-select .el-input__wrapper {
  width: 100%;
  min-width: 180px;
}

/* 分页样式 */
.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

/* 弹窗底部样式 */
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>
