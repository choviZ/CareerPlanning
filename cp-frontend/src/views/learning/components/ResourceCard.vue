<template>
  <el-card
    class="resource-card"
    shadow="hover"
    @click="handleClick"
    :class="{ 'is-top': resource.isTop === 1 }"
  >
    <!-- 置顶标识 -->
    <div v-if="resource.isTop === 1" class="top-badge">
      <el-icon><Star /></el-icon>
      <span>置顶</span>
    </div>

    <!-- 封面或摘要区域 -->
    <div class="cover-section">
      <!-- 有封面时显示封面 -->
      <div v-if="resource.coverImage" class="cover-image">
        <img :src="resource.coverImage" :alt="resource.title" />
      </div>
      <!-- 无封面时显示摘要 -->
      <div v-else class="cover-summary">
        <div class="summary-content">
          <p>{{ displaySummary }}</p>
        </div>
        <div class="summary-overlay">
          <el-icon class="file-icon"><Document /></el-icon>
        </div>
      </div>
    </div>

    <!-- 内容区域 -->
    <div class="content-section">
      <!-- 标题 -->
      <h3 class="resource-title" :title="resource.title">
        {{ resource.title }}
      </h3>

      <!-- 元信息 -->
      <div class="meta-info">
        <div class="meta-row">
          <el-tag
            :type="getResourceTypeTag(resource.resourceType).type"
            size="small"
            class="type-tag"
          >
            {{ getResourceTypeTag(resource.resourceType).text }}
          </el-tag>
          <el-tag
            v-if="resource.category"
            type="info"
            size="small"
            class="category-tag"
          >
            {{ resource.category }}
          </el-tag>
        </div>

        <div class="meta-row">
          <span class="meta-item">
            <el-icon><View /></el-icon>
            {{ resource.viewCount || 0 }}
          </span>
          <span class="meta-item">
            <el-icon><Clock /></el-icon>
            {{ formatDate(resource.createdAt) }}
          </span>
        </div>
      </div>

      <!-- 描述 -->
      <p v-if="resource.summary" class="resource-description">
        {{ truncateText(resource.summary, 80) }}
      </p>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { Star, Document, View, Clock } from '@element-plus/icons-vue'

// 组件属性
interface Props {
  resource: API.LearningResourceVO
}

const props = defineProps<Props>()

// 组件事件
interface Emits {
  click: [resource: API.LearningResourceVO]
}

const emit = defineEmits<Emits>()

/**
 * 显示的摘要内容
 */
const displaySummary = computed(() => {
  if (props.resource.summary) {
    return truncateText(props.resource.summary, 120)
  }
  return '暂无描述信息'
})

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
 * 截断文本
 */
const truncateText = (text: string, maxLength: number): string => {
  if (!text) return ''
  if (text.length <= maxLength) return text
  return text.substring(0, maxLength) + '...'
}

/**
 * 格式化日期
 */
const formatDate = (dateString: string): string => {
  if (!dateString) return ''
  const date = new Date(dateString)
  const now = new Date()
  const diff = now.getTime() - date.getTime()

  // 小于1天显示相对时间
  if (diff < 24 * 60 * 60 * 1000) {
    const hours = Math.floor(diff / (60 * 60 * 1000))
    if (hours < 1) {
      const minutes = Math.floor(diff / (60 * 1000))
      return minutes < 1 ? '刚刚' : `${minutes}分钟前`
    }
    return `${hours}小时前`
  }

  // 大于1天显示具体日期
  return date.toLocaleDateString('zh-CN', {
    month: '2-digit',
    day: '2-digit'
  })
}

/**
 * 卡片点击处理
 */
const handleClick = () => {
  emit('click', props.resource)
}
</script>

<style scoped>
.resource-card {
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.resource-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.resource-card.is-top {
  border: 2px solid #f56c6c;
}

.top-badge {
  position: absolute;
  top: 8px;
  right: 8px;
  background: linear-gradient(135deg, #f56c6c, #ff8a80);
  color: white;
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 4px;
  z-index: 2;
  box-shadow: 0 2px 8px rgba(245, 108, 108, 0.3);
}

.cover-section {
  height: 160px;
  overflow: hidden;
  border-radius: 4px;
  margin-bottom: 16px;
}

.cover-image {
  width: 100%;
  height: 100%;
  position: relative;
}

.cover-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.resource-card:hover .cover-image img {
  transform: scale(1.05);
}

.cover-summary {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  padding: 16px;
}

.summary-content {
  text-align: center;
  z-index: 2;
  position: relative;
}

.summary-content p {
  margin: 0;
  font-size: 14px;
  line-height: 1.5;
  opacity: 0.9;
}

.summary-overlay {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  opacity: 0.2;
  z-index: 1;
}

.file-icon {
  font-size: 48px;
}

.content-section {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.resource-title {
  margin: 0 0 12px 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.meta-info {
  margin-bottom: 12px;
}

.meta-row {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.meta-row:last-child {
  margin-bottom: 0;
}

.type-tag {
  font-weight: 500;
}

.category-tag {
  font-size: 12px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: #909399;
}

.meta-item .el-icon {
  font-size: 14px;
}

.resource-description {
  margin: 0;
  font-size: 13px;
  color: #606266;
  line-height: 1.5;
  flex: 1;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .cover-section {
    height: 140px;
  }

  .resource-title {
    font-size: 15px;
  }

  .summary-content p {
    font-size: 13px;
  }
}
</style>
