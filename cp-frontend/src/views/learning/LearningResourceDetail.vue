<template>
  <div class="resource-detail-container">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="8" animated />
    </div>

    <!-- 资源详情 -->
    <div v-else-if="resource" class="resource-content">
      <!-- 返回按钮 -->
      <div class="back-section">
        <el-button @click="goBack" type="primary" plain>
          <el-icon><ArrowLeft /></el-icon>
          返回列表
        </el-button>
      </div>

      <!-- 资源头部信息 -->
      <el-card class="header-card" shadow="hover">
        <div class="resource-header">
          <!-- 置顶标识 -->
          <div v-if="resource.isTop === 1" class="top-badge">
            <el-icon><Star /></el-icon>
            <span>置顶</span>
          </div>

          <!-- 标题 -->
          <h1 class="resource-title">{{ resource.title }}</h1>

          <!-- 元信息 -->
          <div class="meta-info">
            <div class="meta-row">
              <el-tag
                :type="getResourceTypeTag(resource.resourceType).type"
                size="large"
                class="type-tag"
              >
                {{ getResourceTypeTag(resource.resourceType).text }}
              </el-tag>
              <el-tag
                v-if="resource.category"
                type="info"
                size="large"
                class="category-tag"
              >
                {{ resource.category }}
              </el-tag>
            </div>

            <div class="meta-row">
              <span class="meta-item">
                <el-icon><View /></el-icon>
                浏览量：{{ resource.viewCount || 0 }}
              </span>
              <span class="meta-item">
                <el-icon><Clock /></el-icon>
                发布时间：{{ formatDateTime(resource.createdAt) }}
              </span>
              <span v-if="resource.updatedAt && resource.updatedAt !== resource.createdAt" class="meta-item">
                <el-icon><Edit /></el-icon>
                更新时间：{{ formatDateTime(resource.updatedAt) }}
              </span>
            </div>
          </div>
          <!-- 描述 -->
          <p v-if="resource.summary" class="resource-description">
            {{ resource.summary }}
          </p>
        </div>
      </el-card>

      <!-- 封面图片 -->
      <el-card v-if="resource.coverImage" class="cover-card" shadow="hover">
        <div class="cover-container">
          <img :src="resource.coverImage" :alt="resource.title" class="cover-image" />
        </div>
      </el-card>

      <!-- Markdown 内容 -->
      <el-card class="content-card" shadow="hover">
        <div class="markdown-container">
          <MarkdownRenderer :content="resource.content || '暂无内容'" />
        </div>
      </el-card>
    </div>

    <!-- 错误状态 -->
    <div v-else class="error-container">
      <el-result
        icon="warning"
        title="资源不存在"
        sub-title="抱歉，您访问的学习资源不存在或已被删除"
      >
        <template #extra>
          <el-button type="primary" @click="goBack">
            返回列表
          </el-button>
        </template>
      </el-result>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowLeft, Star, View, Clock, Edit } from '@element-plus/icons-vue'
import { getLearningResourceById } from '@/api/xuexiziyuanguanli'
import MarkdownRenderer from './components/MarkdownRenderer.vue'

// 路由
const route = useRoute()
const router = useRouter()

// 响应式数据
const loading = ref(false)
const resource = ref<API.LearningResourceVO | null>(null)

/**
 * 获取资源类型标签配置
 */
const getResourceTypeTag = (type: number) => {
  const typeMap = {
    1: { text: '文章', type: 'primary' },
    2: { text: '视频', type: 'success' },
    3: { text: '文档', type: 'warning' }
  }
  return typeMap[type as keyof typeof typeMap] || { text: '其他', type: 'info' }
}

/**
 * 格式化日期时间
 */
const formatDateTime = (dateString: string): string => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

/**
 * 加载资源详情
 */
const loadResourceDetail = async () => {
  const resourceId = route.params.id
  loading.value = true
  try {
    const res = await getLearningResourceById({ resourceId })
    if (res.data.code === 200 && res.data.data) {
      resource.value = res.data.data
    } else {
      ElMessage.error(res.data.message || '获取资源详情失败')
    }
  } catch (error) {
    console.error('获取资源详情失败:', error)
    ElMessage.error('获取资源详情失败')
  } finally {
    loading.value = false
  }
}

/**
 * 返回列表页
 */
const goBack = () => {
  router.push({ name: 'LearningResource' })
}

// 页面加载时获取数据
onMounted(() => {
  loadResourceDetail()
})
</script>

<style scoped>
.resource-detail-container {
  padding: 20px;
  max-width: 1000px;
  margin: 0 auto;
}

.loading-container {
  padding: 40px;
}

.back-section {
  margin-bottom: 20px;
}

.header-card {
  margin-bottom: 20px;
}

.resource-header {
  position: relative;
}

.top-badge {
  position: absolute;
  top: 0;
  right: 0;
  background: linear-gradient(135deg, #f56c6c, #ff8a80);
  color: white;
  padding: 6px 12px;
  border-radius: 16px;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 6px;
  box-shadow: 0 2px 8px rgba(245, 108, 108, 0.3);
}

.resource-title {
  margin: 0 0 20px 0;
  font-size: 32px;
  font-weight: 700;
  color: #303133;
  line-height: 1.3;
  padding-right: 100px;
}

.meta-info {
  margin-bottom: 20px;
}

.meta-row {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
  flex-wrap: wrap;
}

.meta-row:last-child {
  margin-bottom: 0;
}

.type-tag {
  font-weight: 600;
}

.category-tag {
  font-weight: 500;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  color: #606266;
}

.meta-item .el-icon {
  font-size: 16px;
}

.resource-description {
  margin: 0 0 20px 0;
  font-size: 16px;
  color: #606266;
  line-height: 1.6;
  background: #f8f9fa;
  padding: 16px;
  border-radius: 8px;
  border-left: 4px solid #409eff;
}

.external-link {
  margin-top: 16px;
}

.cover-card {
  margin-bottom: 20px;
}

.cover-container {
  text-align: center;
}

.cover-image {
  max-width: 100%;
  height: auto;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.content-card {
  margin-bottom: 20px;
}

.markdown-container {
  min-height: 200px;
}

.error-container {
  padding: 60px 20px;
  text-align: center;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .resource-detail-container {
    padding: 16px;
  }

  .resource-title {
    font-size: 24px;
    padding-right: 0;
    margin-bottom: 16px;
  }

  .top-badge {
    position: static;
    margin-bottom: 16px;
    align-self: flex-start;
  }

  .meta-row {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }

  .resource-description {
    font-size: 15px;
  }
}
</style>
