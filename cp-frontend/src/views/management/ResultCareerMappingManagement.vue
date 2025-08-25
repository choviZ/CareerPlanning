<template>
  <div class="result-career-mapping-management">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>评估结果职业映射管理</span>
          <el-button type="primary" @click="handleAdd">新增映射</el-button>
        </div>
      </template>

      <!-- 搜索区域 -->
      <el-form :inline="true" :model="searchParams" class="search-form">
        <el-form-item label="测试类型">
          <el-select v-model="searchParams.testType" placeholder="请选择测试类型" clearable style="width: 120px;">
            <el-option label="MBTI" value="MBTI"></el-option>
            <el-option label="Holland" value="Holland"></el-option>
            <el-option label="BigFive" value="BigFive"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="结果代码">
          <el-input v-model="searchParams.resultCode" placeholder="请输入结果代码"></el-input>
        </el-form-item>
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
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="testType" label="测试类型" width="120"></el-table-column>
        <el-table-column prop="resultCode" label="结果代码" width="120"></el-table-column>
        <el-table-column prop="careerName" label="职业名称" width="180"></el-table-column>
        <el-table-column prop="description" label="职业描述" show-overflow-tooltip></el-table-column>
        <el-table-column prop="compatibilityScore" label="兼容性分数" width="120">
          <template #default="{ row }">
            <el-tag :type="getScoreType(row.compatibilityScore)">{{ row.compatibilityScore }}%</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" :icon="Edit" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" :icon="Delete" size="small" @click="handleDelete(row)">删除</el-button>
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
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
        <el-form-item label="测试类型" prop="testType">
          <el-select
            v-model="form.testType"
            placeholder="请选择测试类型"
            style="width: 100%"
            @change="handleTestTypeOrResultCodeChange"
          >
            <el-option label="MBTI" value="MBTI"></el-option>
            <el-option label="Holland" value="Holland"></el-option>
            <el-option label="BigFive" value="BigFive"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="结果代码" prop="resultCode">
          <el-input
            v-model="form.resultCode"
            placeholder="请输入结果代码"
            @blur="handleTestTypeOrResultCodeChange"
          ></el-input>
        </el-form-item>
        <el-form-item label="评估结果ID">
          <el-input
            v-model="form.assessmentResultId"
            placeholder="将根据测试类型和结果代码自动获取"
            :loading="resultLoading"
            readonly
          >
            <template #suffix>
              <el-icon v-if="resultLoading" class="is-loading">
                <Loading />
              </el-icon>
              <el-icon v-else-if="form.assessmentResultId" style="color: #67c23a">
                <Check />
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="职业" prop="careerId">
          <el-select
            v-model="form.careerId"
            placeholder="请选择职业"
            style="width: 100%"
            filterable
            remote
            :remote-method="searchCareers"
            :loading="careerLoading"
          >
            <el-option
              v-for="career in careerOptions"
              :key="career.id"
              :label="career.name"
              :value="career.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="兼容性分数" prop="compatibilityScore">
          <el-slider
            v-model="form.compatibilityScore"
            :min="0"
            :max="100"
            show-input
            :show-input-controls="false"
            style="width: 100%"
          ></el-slider>
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
import { ref, onMounted, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Edit, Delete, Loading, Check } from '@element-plus/icons-vue'
import {
  addResultCareerMapping,
  deleteResultCareerMapping,
  queryMappingByResultCode,
  updateResultCareerMapping
} from '@/api/resultCareerMappingController'
import { listCareerByPage } from '@/api/careerController'

// 搜索参数
const searchParams = ref({
  testType: '',
  resultCode: '',
  careerName: ''
})

// 表格数据
const tableData = ref<API.ResultCareerMapping[]>([])
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
const submitLoading = ref(false)
const formRef = ref()

// 表单数据
const form = ref({
  id: undefined as number | undefined,
  testType: '',
  resultCode: '',
  careerId: undefined as number | undefined,
  compatibilityScore: 50,
  assessmentResultId: undefined as number | undefined
})

// 表单验证规则
const rules = reactive({
  testType: [{ required: true, message: '请选择测试类型', trigger: 'change' }],
  resultCode: [{ required: true, message: '请输入结果代码', trigger: 'blur' }],
  careerId: [{ required: true, message: '请选择职业', trigger: 'change' }],
  compatibilityScore: [{ required: true, message: '请设置兼容性分数', trigger: 'blur' }]
})

// 职业选项
const careerOptions = ref<API.CareerVo[]>([])
const careerLoading = ref(false)

// 评估结果选项
const resultLoading = ref(false)

// 根据分数获取标签类型
const getScoreType = (score: number) => {
  if (score >= 80) return 'success'
  if (score >= 60) return 'warning'
  return 'danger'
}

// 搜索职业
const searchCareers = async (query: string) => {
  if (!query) {
    careerOptions.value = []
    return
  }
  careerLoading.value = true
  try {
    const res = await listCareerByPage({
      name: query,
      current: 1,
      pageSize: 20
    })
    if (res.data.code === 200 && res.data.data) {
      careerOptions.value = res.data.data.records || []
    }
  } catch (error) {
    console.error('搜索职业失败:', error)
  } finally {
    careerLoading.value = false
  }
}

// 根据测试类型和结果代码查询评估结果ID
const findAssessmentResultId = async (testType: string, resultCode: string): Promise<number | undefined> => {
  if (!testType || !resultCode) return undefined

  try {
    // 通过查询现有映射来获取resultId
    const res = await queryMappingByResultCode({
      testType,
      resultCode,
      pageNum: 1,
      pageSize: 1
    })
    if (res.data.code === 200 && res.data.data && res.data.data.records && res.data.data.records.length > 0) {
      return res.data.data.records[0].resultId
    }
    ElMessage.warning(`未找到测试类型 ${testType} 结果代码 ${resultCode} 对应的评估结果`)
    return undefined
  } catch (error) {
    console.error('查询评估结果ID失败:', error)
    return undefined
  }
}

// 当测试类型或结果代码改变时，自动查询评估结果ID
const handleTestTypeOrResultCodeChange = async () => {
  if (form.value.testType && form.value.resultCode) {
    resultLoading.value = true
    try {
      const resultId = await findAssessmentResultId(form.value.testType, form.value.resultCode)
      form.value.assessmentResultId = resultId
      if (resultId) {
        ElMessage.success('已自动获取评估结果ID')
      }
    } finally {
      resultLoading.value = false
    }
  } else {
    form.value.assessmentResultId = undefined
  }
}

// 加载表格数据
const loadTableData = async () => {
  loading.value = true
  try {
    const params: any = {
      pageNum: pagination.value.currentPage,
      pageSize: pagination.value.pageSize
    }

    // 如果有搜索条件，添加到参数中
    if (searchParams.value.testType) {
      params.testType = searchParams.value.testType
    }
    if (searchParams.value.resultCode) {
      params.resultCode = searchParams.value.resultCode
    }

    const res = await queryMappingByResultCode(params)
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

// 搜索
const handleSearch = () => {
  pagination.value.currentPage = 1
  loadTableData()
}

// 重置
const handleReset = () => {
  searchParams.value = {
    testType: '',
    resultCode: '',
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
  dialogTitle.value = '新增映射'
  form.value = {
    id: undefined,
    testType: '',
    resultCode: '',
    careerId: undefined,
    compatibilityScore: 50,
    assessmentResultId: undefined
  }
  careerOptions.value = []
  dialogVisible.value = true
}

// 编辑
const handleEdit = async (row: API.ResultCareerMapping) => {
  dialogTitle.value = '编辑映射'
  form.value = {
    id: row.id,
    testType: row.testType || '',
    resultCode: row.resultCode || '',
    careerId: row.careerId,
    compatibilityScore: row.compatibilityScore || 50,
    assessmentResultId: row.resultId // 使用现有的resultId
  }

  // 如果有职业ID，加载对应的职业信息
  if (row.careerId && row.careerName) {
    careerOptions.value = [{
      id: row.careerId,
      name: row.careerName,
      description: row.description
    } as API.CareerVo]
  }

  dialogVisible.value = true
}

// 删除
const handleDelete = (row: API.ResultCareerMapping) => {
  ElMessageBox.confirm(
    `确认删除映射【${row.testType} - ${row.resultCode} - ${row.careerName}】吗？`,
    '提示',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const res = await deleteResultCareerMapping({ id: row.id! })
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

// 弹窗确认
const handleConfirm = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()

    // 检查是否已获取到评估结果ID
    if (!form.value.assessmentResultId) {
      ElMessage.error('请先选择测试类型和结果代码以获取评估结果ID')
      return
    }

    submitLoading.value = true

    let res
    if (form.value.id) {
      // 编辑
      const updateData: API.UpdateResultCareerRequest = {
        id: form.value.id,
        assessmentResultId: form.value.assessmentResultId,
        careerId: form.value.careerId,
        compatibilityScore: form.value.compatibilityScore
      }
      res = await updateResultCareerMapping(updateData)
    } else {
      // 新增
      const addData = {
        assessmentResultId: form.value.assessmentResultId,
        careerId: form.value.careerId!,
        compatibilityScore: form.value.compatibilityScore
      }
      res = await addResultCareerMapping(addData)
    }

    if (res.data.code === 200) {
      ElMessage.success(form.value.id ? '编辑成功' : '新增成功')
      dialogVisible.value = false
      await loadTableData()
    } else {
      ElMessage.error(res.data.message || '操作失败')
    }
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error('操作失败')
  } finally {
    submitLoading.value = false
  }
}

// 页面加载时获取数据
onMounted(() => {
  loadTableData()
})
</script>

<style scoped>
.result-career-mapping-management {
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

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>
