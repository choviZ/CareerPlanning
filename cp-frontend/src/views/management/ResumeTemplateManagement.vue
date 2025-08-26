<template>
  <div class="resume-template-management">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>简历模板管理</span>
          <el-button type="primary" @click="handleAdd">新增模板</el-button>
        </div>
      </template>

      <!-- 搜索区域 -->
      <el-form :inline="true" :model="searchParams" class="search-form">
        <el-form-item label="模板名称">
          <el-input v-model="searchParams.templateName" placeholder="请输入模板名称"></el-input>
        </el-form-item>
        <el-form-item label="模板类型">
          <el-select v-model="searchParams.templateType" placeholder="请选择模板类型" clearable>
            <el-option label="基础模板" :value="1"></el-option>
            <el-option label="高级模板" :value="2"></el-option>
            <el-option label="专业模板" :value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchParams.isActive" placeholder="请选择状态" clearable>
            <el-option label="启用" :value="1"></el-option>
            <el-option label="禁用" :value="0"></el-option>
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
        <el-table-column prop="templateName" label="模板名称" width="200"></el-table-column>
        <el-table-column prop="description" label="描述" show-overflow-tooltip></el-table-column>
        <el-table-column prop="templateType" label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getTemplateTypeTag(row.templateType)">
              {{ getTemplateTypeName(row.templateType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sortOrder" label="排序" width="100"></el-table-column>
        <el-table-column prop="isActive" label="状态" width="100">
          <template #default="{ row }">
            <el-switch
              v-model="row.isActive"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(row)"
            ></el-switch>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="260" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" :icon="View" size="small" @click="handlePreview(row)">预览
            </el-button>
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
      ></el-pagination>
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="800px"
               :close-on-click-modal="false">
      <el-form :model="form" :rules="formRules" ref="formRef" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="模板名称" prop="templateName">
              <el-input v-model="form.templateName" placeholder="请输入模板名称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模板类型" prop="templateType">
              <el-select v-model="form.templateType" placeholder="请选择模板类型">
                <el-option label="基础模板" :value="1"></el-option>
                <el-option label="高级模板" :value="2"></el-option>
                <el-option label="专业模板" :value="3"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="模板描述" prop="description">
          <el-input v-model="form.templateDesc" type="textarea" :rows="3"
                    placeholder="请输入模板描述"></el-input>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sortOrder" :min="0"
                           placeholder="数字越小排序越靠前"></el-input-number>
        </el-form-item>

      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleConfirm" :loading="submitLoading">确认</el-button>
        </span>
      </template>
    </el-dialog>


    <!-- 预览弹窗 -->
    <el-dialog v-model="previewVisible" title="模板预览" width="90%" :close-on-click-modal="false">
      <div class="preview-container">
        <ResumePreview
          v-if="previewTemplate"
          :resume-data="{
            content: previewTemplate.defaultContent || {},
            templateConfig: previewTemplate.templateConfig
          }"
          :template-config="previewTemplate.templateConfig"
        />
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance } from 'element-plus'
import { Edit, Delete, View } from '@element-plus/icons-vue'
import {
  queryResumeTemplate,
  addResumeTemplate,
  updateResumeTemplate,
  deleteResumeTemplate,
  updateTemplateStatus
} from '@/api/jianlimobanguanli'

// 搜索参数
const searchParams = ref<API.ResumeTemplateQueryRequest>({
  templateName: '',
  templateType: undefined,
  isActive: undefined
})

// 表格数据
const tableData = ref<API.ResumeTemplateVo[]>([])
const loading = ref(false)

// 分页数据
const pagination = ref({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 弹窗
const dialogVisible = ref(false)
const dialogTitle = ref('')
const submitLoading = ref(false)
const formRef = ref<FormInstance>()

// 表单数据
const form = ref<API.ResumeTemplateVo>({
  id: '',
  templateName: '',
  templateDesc: '',
  templateConfig: {},
  templateType: 1,
  sortOrder: 0
})


/**
 * 表单验证规则
 */
const formRules = {
  templateName: [
    { required: true, message: '请输入模板名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入模板描述', trigger: 'blur' },
    { min: 5, max: 200, message: '长度在 5 到 200 个字符', trigger: 'blur' }
  ],
  templateType: [
    { required: true, message: '请选择模板类型', trigger: 'change' }
  ],
  sortOrder: [
    { type: 'number', min: 0, message: '排序值不能小于0', trigger: 'blur' }
  ]
}

// 预览相关
const previewVisible = ref(false)
const previewTemplate = ref<API.ResumeTemplateVo | null>(null)

/**
 * 加载表格数据
 */
const loadTableData = async () => {
  loading.value = true
  try {
    const params: API.ResumeTemplateQueryRequest = {
      current: pagination.value.currentPage,
      pageSize: pagination.value.pageSize
    }
    // 添加搜索条件
    if (searchParams.value.templateName) {
      params.templateName = searchParams.value.templateName
    }
    if (searchParams.value.templateType !== undefined && searchParams.value.templateType !== null) {
      params.templateType = searchParams.value.templateType
    }
    if (searchParams.value.isActive !== undefined && searchParams.value.isActive !== null) {
      params.isActive = searchParams.value.isActive
    }
    const res = await queryResumeTemplate(params)
    if (res.data.code === 200 && res.data.data) {
      tableData.value = res.data.data.records || []
      pagination.value.total = Number(res.data.data.totalRow) || 0
    } else {
      ElMessage.error(res.data.message || '获取数据失败')
    }
  } catch (error) {
    ElMessage.error('获取数据失败' + error)
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
 * 重置
 */
const handleReset = () => {
  searchParams.value = {
    templateName: '',
    templateType: undefined,
    isActive: undefined
  }
  pagination.value.currentPage = 1
  loadTableData()
}

/**
 * 分页变化
 * @param page
 */
const handleCurrentChange = (page: number) => {
  pagination.value.currentPage = page
  loadTableData()
}

const handleSizeChange = (size: number) => {
  pagination.value.pageSize = size
  pagination.value.currentPage = 1
  loadTableData()
}

/**
 * 新增
 */
const handleAdd = () => {
  dialogTitle.value = '新增模板'
  form.value = {
    templateName: '',
    templateDesc: '',
    templateConfig: {
      layout: {
        type: 'single-column'
      },
      colors: {
        primary: '#2c3e50',
        secondary: '#3498db',
        text: '#2c3e50',
        background: '#ffffff'
      },
      typography: {
        fontFamily: '\'Microsoft YaHei\', \'PingFang SC\', sans-serif',
        fontSize: '14px',
        lineHeight: '1.6'
      }
    },
    templateType: 1,
    sortOrder: 0
  }
  dialogVisible.value = true
}

/**
 * 编辑
 * @param row
 */
const handleEdit = (row: API.ResumeTemplateUpdateRequest) => {
  dialogTitle.value = '编辑模板'
  form.value = {
    id: row.id,
    templateName: row.templateName || '',
    templateDesc: row.templateDesc || '',
    templateType: row.templateType || 1,
    templateConfig: row.templateConfig || {},
    defaultContent: row.defaultContent || {},
    sortOrder: row.sortOrder || 0
  }
  dialogVisible.value = true
}

/**
 * 预览
 * @param row
 */
const handlePreview = (row: API.ResumeTemplateVo) => {
  previewTemplate.value = row
  previewVisible.value = true
}

/**
 * 删除
 * @param row
 */
const handleDelete = (row: API.ResumeTemplateVo) => {
  ElMessageBox.confirm(
    `确定要删除模板"${row.templateName}"吗？删除后不可恢复！`,
    '删除确认',
    {
      confirmButtonText: '确定删除',
      cancelButtonText: '取消',
      type: 'warning',
      dangerouslyUseHTMLString: false
    }
  ).then(async () => {
    try {
      const res = await deleteResumeTemplate({ id: row.id! })
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
  }).catch(() => {
    // 用户取消删除，不需要处理
  })
}

/**
 * 状态切换
 * @param row
 */
const handleStatusChange = async (row: API.ResumeTemplateVo) => {
  const statusText = row.isActive === 1 ? '启用' : '禁用'
  try {
    const res = await updateTemplateStatus({
      id: row.id!,
      status: row.isActive!
    })
    if (res.data.code === 200) {
      ElMessage.success(`${statusText}成功`)
      await loadTableData() // 重新加载数据确保状态同步
    } else {
      ElMessage.error(res.data.message || `${statusText}失败`)
      // 恢复原状态
      row.isActive = row.isActive === 1 ? 0 : 1
    }
  } catch (error) {
    console.error(`${statusText}失败:`, error)
    ElMessage.error(`${statusText}失败`)
    // 恢复原状态
    row.isActive = row.isActive === 1 ? 0 : 1
  }
}

/**
 * 弹窗确认
 */
const handleConfirm = async () => {
  if (!formRef.value) return
  try {
    // 表单验证
    const valid = await formRef.value.validate()
    if (!valid) {
      return false
    }
    submitLoading.value = true
    // 准备提交数据
    const submitData = {
      templateName: form.value.templateName,
      description: form.value.templateDesc,
      templateType: form.value.templateType,
      templateConfig: form.value.templateConfig || {},
      defaultContent: form.value.defaultContent || {},
      sortOrder: form.value.sortOrder || 0
    }
    let res
    if (form.value.id) {
      // 编辑
      const updateData: API.ResumeTemplateUpdateRequest = {
        id: form.value.id,
        ...submitData
      }
      res = await updateResumeTemplate(updateData)
    } else {
      // 新增
      const addData: API.ResumeTemplateAddRequest = submitData
      res = await addResumeTemplate(addData)
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
    if (error !== false) { // 表单验证失败时error为false
      ElMessage.error('操作失败')
    }
  } finally {
    submitLoading.value = false
  }
}


/**
 * 获取模板类型名称
 * @param type
 */
const getTemplateTypeName = (type: number) => {
  const typeMap: Record<number, string> = {
    1: '基础',
    2: '高级',
    3: '专业'
  }
  return typeMap[type] || '未知'
}

/**
 * 获取模板类型标签颜色
 * @param type
 */
const getTemplateTypeTag = (type: number) => {
  const tagMap: Record<number, string> = {
    1: '',
    2: 'success',
    3: 'warning'
  }
  return tagMap[type] || ''
}

/**
 * 格式化日期
 * @param dateStr
 */
const formatDate = (dateStr: string) => {
  if (!dateStr) return '-'
  return new Date(dateStr).toLocaleString('zh-CN')
}

// 页面加载时获取数据
onMounted(() => {
  loadTableData()
})
</script>

<style scoped>
.resume-template-management {
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


.preview-container {
  height: 600px;
  overflow-y: auto;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 20px;
  background-color: #f5f7fa;
}
</style>
