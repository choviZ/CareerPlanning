<template>
  <div class="resume-template-gallery">
    <div class="header-actions">
      <el-input
        v-model="searchParams.templateName"
        placeholder="搜索模板名称"
        style="width: 200px; margin-right: 10px;"
        clearable
        @keyup.enter="handleSearch"
      >
        <template #prefix>
          <el-icon>
            <Search />
          </el-icon>
        </template>
      </el-input>
      <el-select
        v-model="searchParams.templateType"
        placeholder="模板类型"
        style="width: 120px; margin-right: 10px;"
        clearable
      >
        <el-option label="基础模板" :value="1"></el-option>
        <el-option label="高级模板" :value="2"></el-option>
        <el-option label="专业模板" :value="3"></el-option>
      </el-select>
      <el-button type="primary" @click="handleSearch">
        <el-icon>
          <Search />
        </el-icon>
        搜索
      </el-button>
    </div>

    <!-- 模板卡片网格 -->
    <div v-loading="loading" class="template-grid">
      <div
        v-for="template in templateList"
        :key="template.id"
        class="template-card-wrapper"
        @click="handleSelectTemplate(template)"
      >
        <el-card class="template-card" shadow="hover">
          <!-- 预览图区域 -->
          <div class="preview-area">
            <!-- 优先显示生成的预览图片 -->
            <div v-if="previewImages.get(template.id?.toString())" class="preview-image">
              <img :src="previewImages.get(template.id?.toString())" :alt="template.templateName" />
            </div>
            <!-- 其次显示原有的预览图片 -->
            <div v-else-if="template.previewUrl" class="preview-image">
              <img :src="template.previewUrl" :alt="template.templateName" />
            </div>
            <!-- 正在生成预览图片时显示加载状态 -->
            <div v-else-if="generatingImages.has(template.id?.toString())" class="preview-loading">
              <el-icon class="is-loading">
                <Loading />
              </el-icon>
              <span>生成预览中...</span>
            </div>
            <!-- 最后显示组件预览 -->
            <div v-else class="preview-placeholder">
              <ResumePreview
                :resume-data="{ content: getPreviewData(template) }"
                :template-config="template.templateConfig"
                class="mini-preview"
              />
            </div>

            <!-- 悬浮操作按钮 -->
            <div class="preview-overlay">
              <el-button type="primary" size="small" @click.stop="handlePreview(template)">
                <el-icon>
                  <View />
                </el-icon>
                预览
              </el-button>
              <el-button type="success" size="small" @click.stop="handleUseTemplate(template)">
                <el-icon><Select /></el-icon>
                使用
              </el-button>
            </div>
          </div>
          <template #footer>
            <!-- 模板信息 -->
            <div class="template-info">
              <h3 class="template-name">{{ template.templateName }}</h3>
              <p class="template-desc">{{ template.templateDesc || '暂无描述' }}</p>
              <div class="template-meta">
                <el-tag :type="getTemplateTypeTag(template.templateType)" size="small">
                  {{ getTemplateTypeName(template.templateType) }}
                </el-tag>
                <span class="template-date">{{ formatDate(template.updatedAt) }}</span>
              </div>
            </div>
          </template>

        </el-card>
      </div>

      <!-- 空状态 -->
      <div v-if="!loading && templateList.length === 0" class="empty-state">
        <el-empty description="暂无模板数据">
          <el-button type="primary" @click="handleSearch">刷新</el-button>
        </el-empty>
      </div>
    </div>

    <!-- 分页组件 -->
    <el-pagination
      v-if="templateList.length > 0"
      class="pagination"
      :current-page="pagination.currentPage"
      :page-size="pagination.pageSize"
      :total="pagination.total"
      :page-sizes="[12, 24, 36, 48]"
      layout="total, sizes, prev, pager, next, jumper"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    ></el-pagination>

    <!-- 预览弹窗 -->
    <ResumePreviewDialog
      v-model="previewVisible"
      title="模板预览"
      :resume-data="selectedTemplate ? {
        content: getPreviewData(selectedTemplate)
      } : null"
      :template-config="selectedTemplate?.templateConfig"
      confirm-text="使用此模板"
      @confirm="useTemplate"
      @cancel="closePreview"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, View, Select, Loading } from '@element-plus/icons-vue'
import { queryResumeTemplate } from '@/api/jianlimobanguanli'
import ResumePreview from '@/components/resume/ResumePreview.vue'
import ResumePreviewDialog from '@/components/resume/ResumePreviewDialog.vue'
import { useRouter } from 'vue-router'
import { generateResumePreviewImage, previewImageCache } from '@/utils/previewGenerator'

const router = useRouter()

// 响应式数据
const loading = ref(false)
const templateList = ref<API.ResumeTemplateVo[]>([])
const previewVisible = ref(false)
const selectedTemplate = ref<API.ResumeTemplateVo | null>(null)
const previewImages = ref<Map<string, string>>(new Map()) // 存储生成的预览图片
const generatingImages = ref<Set<string>>(new Set()) // 正在生成图片的模板ID

// 搜索参数
const searchParams = reactive({
  templateName: '',
  templateType: undefined as number | undefined,
  isActive: 1 // 只显示启用的模板
})

// 分页参数
const pagination = reactive({
  currentPage: 1,
  pageSize: 12,
  total: 0
})

// 生成单个模板的预览图片
const generateTemplatePreviewImage = async (template: API.ResumeTemplateVo) => {
  if (!template.id || generatingImages.value.has(template.id.toString())) {
    return
  }

  try {
    // 检查缓存
    const cachedImage = previewImageCache.get(template.id, template.templateConfig)
    if (cachedImage) {
      previewImages.value.set(template.id.toString(), cachedImage)
      return
    }

    generatingImages.value.add(template.id.toString())

    const imageData = await generateResumePreviewImage(
      template.defaultContent || {},
      template.templateConfig
    )

    // 存储到缓存和响应式数据中
    previewImageCache.set(template.id, template.templateConfig, imageData)
    previewImages.value.set(template.id.toString(), imageData)
  } catch (error) {
    console.error(`生成模板 ${template.id} 预览图片失败:`, error)
  } finally {
    generatingImages.value.delete(template.id.toString())
  }
}

// 批量生成预览图片
const generateAllPreviewImages = async () => {
  for (const template of templateList.value) {
    // 异步生成，不阻塞UI
    generateTemplatePreviewImage(template)
  }
}

// 获取模板列表
const fetchTemplateList = async () => {
  loading.value = true
  const params: API.ResumeTemplateQueryRequest = {
    current: pagination.currentPage,
    pageSize: pagination.pageSize,
    templateName: searchParams.templateName || undefined,
    templateType: searchParams.templateType,
    isActive: searchParams.isActive
  }

  const response = await queryResumeTemplate(params)
  if (response.data.code === 200 && response.data.data) {
    templateList.value = response.data.data.records || []
    pagination.total = response.data.data.totalRow || 0

    // 获取模板列表后生成预览图片
    generateAllPreviewImages()
  } else {
    ElMessage.error(response.data.message || '获取模板列表失败')
  }
  loading.value = false
}

// 搜索处理
const handleSearch = () => {
  pagination.currentPage = 1
  fetchTemplateList()
}

// 分页处理
const handleCurrentChange = (page: number) => {
  pagination.currentPage = page
  fetchTemplateList()
}

const handleSizeChange = (size: number) => {
  pagination.pageSize = size
  pagination.currentPage = 1
  fetchTemplateList()
}

// 预览处理
const handlePreview = (template: API.ResumeTemplateVo) => {
  selectedTemplate.value = template
  previewVisible.value = true
}

// 选择模板
const handleSelectTemplate = (template: API.ResumeTemplateVo) => {
  handlePreview(template)
}

// 使用模板（从卡片直接使用）
const handleUseTemplate = async (template: API.ResumeTemplateVo) => {
  try {
    await ElMessageBox.confirm(
      `确定要使用模板「${template.templateName}」创建简历吗？`,
      '确认使用模板',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }
    )

    // 跳转到简历编辑页面，传递模板ID
    router.push({
      path: '/resume/edit',
      query: { templateId: template.id }
    })
  } catch {
    // 用户取消
  }
}

// 使用模板（从预览弹窗）
const useTemplate = () => {
  if (selectedTemplate.value) {
    // 跳转到简历编辑页面，传递模板ID
    router.push({
      path: '/resume/edit',
      query: {
        templateId: selectedTemplate.value.id
      }
    })
    ElMessage.success('已选择模板，正在跳转到编辑页面...')
    closePreview()
  }
}

// 关闭预览
const closePreview = () => {
  previewVisible.value = false
  selectedTemplate.value = null
}

// 获取预览数据
const getPreviewData = (template: API.ResumeTemplateVo): API.ResumeContent => {
  return template.defaultContent || {}
}

// 工具函数
const getTemplateTypeName = (type: number | undefined) => {
  if (type === undefined) return '未知类型'
  const typeMap: Record<number, string> = {
    1: '基础模板',
    2: '高级模板',
    3: '专业模板'
  }
  return typeMap[type] || '未知类型'
}

const getTemplateTypeTag = (type: number | undefined) => {
  if (type === undefined) return ''
  const tagMap: Record<number, string> = {
    1: '',
    2: 'warning',
    3: 'success'
  }
  return tagMap[type] || ''
}

const formatDate = (dateStr: string | undefined) => {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleDateString('zh-CN')
}

// 初始化
onMounted(() => {
  fetchTemplateList()
})
</script>

<style scoped>
.resume-template-gallery {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 18px;
  font-weight: 600;
}

.header-actions {
  display: flex;
  align-items: center;
}

.template-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  margin: 20px 0;
  min-height: 400px;
}

.template-card-wrapper {
  cursor: pointer;
  transition: transform 0.2s ease;
}

.template-card-wrapper:hover {
  transform: translateY(-5px);
}

.template-card {
  height: 100%;
  position: relative;
  overflow: hidden;
}

.preview-area {
  position: relative;
  height: 200px;
  background: #f5f7fa;
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 15px;
}

.preview-image {
  width: 100%;
  height: 100%;
}

.preview-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.preview-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fafafa;
}

.mini-preview {
  transform: scale(0.25);
  transform-origin: top left;
  width: 400%;
  height: 400%;
  pointer-events: none;
  overflow: hidden;
}

.preview-placeholder {
  width: 100%;
  height: 100%;
  overflow: hidden;
  position: relative;
  background-color: #ffffff;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
}

.preview-loading {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: #fafafa;
  color: #909399;
  font-size: 14px;
}

.preview-loading .el-icon {
  font-size: 24px;
  margin-bottom: 8px;
}

.preview-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.template-card-wrapper:hover .preview-overlay {
  opacity: 1;
}

.template-info {
  padding: 0 5px;
}

.template-name {
  font-size: 16px;
  font-weight: 600;
  margin: 0 0 8px 0;
  color: #303133;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.template-desc {
  font-size: 14px;
  color: #606266;
  margin: 0 0 12px 0;
  line-height: 1.4;
  height: 2.8em;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.template-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.template-date {
  font-size: 12px;
  color: #909399;
}

.empty-state {
  grid-column: 1 / -1;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 300px;
}

.pagination {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}

/* 预览弹窗样式已移至 ResumePreviewDialog 组件中 */

/* 响应式设计 */
@media (max-width: 768px) {
  .template-grid {
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
    gap: 15px;
  }

  .header-actions {
    flex-direction: column;
    gap: 10px;
    align-items: stretch;
  }

  .header-actions .el-input,
  .header-actions .el-select {
    width: 100% !important;
    margin-right: 0 !important;
  }
}

@media (max-width: 480px) {
  .template-grid {
    grid-template-columns: 1fr;
  }

  .card-header {
    flex-direction: column;
    align-items: stretch;
    gap: 15px;
  }
}
</style>
