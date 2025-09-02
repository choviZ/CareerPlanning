<template>
  <el-card class="post-card" shadow="hover" @click="goToDetail">
    <!-- 用户信息头部 -->
    <div class="post-header">
      <div class="user-info">
        <el-avatar
          :size="40"
          :src="post.user?.userAvatar"
          class="user-avatar"
        >
          <img src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" />
        </el-avatar>
        <div class="user-details">
          <div class="username">{{ post.user?.userName || '匿名用户' }}</div>
          <div class="post-time">{{ formatTime(post.createdAt) }}</div>
        </div>
      </div>
      <div class="post-meta">
        <el-tag v-if="post.isEssence" type="warning" size="small" class="essence-tag">
          精华
        </el-tag>
      </div>
    </div>

    <!-- 帖子内容 -->
    <div class="post-content">
      <div v-if="post.title" class="post-title">{{ post.title }}</div>
      <div class="post-text">{{ post.content }}</div>

      <!-- 标签 -->
      <div v-if="post.tagList" class="post-tags">
        <el-tag
          v-for="tag in parseTags(post.tagList)"
          :key="tag"
          size="small"
          type="info"
          class="tag-item"
        >
          {{ tag }}
        </el-tag>
      </div>
    </div>

    <!-- 操作按钮 -->
    <div class="post-actions">
      <div class="action-group">
        <el-button
          :type="post.hasLiked ? 'primary' : 'default'"
          size="small"
          @click.stop="handleLike"
          class="action-btn"
        >
          <svg class="icon" viewBox="0 0 1024 1024" width="16" height="16">
            <path d="M885.9 533.7c16.8-22.2 26.1-49.4 26.1-77.7 0-44.9-25.1-87.4-65.5-111.1a67.67 67.67 0 0 0-34.3-9.3H572.4l6-122.9c1.4-29.7-9.1-57.9-29.5-79.4A106.62 106.62 0 0 0 471 99.9c-52 0-98 35-111.8 85.1l-85.9 311H144c-17.7 0-32 14.3-32 32v364c0 17.7 14.3 32 32 32h601.3c9.2 0 18.2-1.8 26.8-5.4 99.3-41.3 180.7-131 180.7-235.1 0-52.7-19.7-101.9-55.9-139.5z" fill="currentColor"/>
          </svg>
          {{ post.likeCount || 0 }}
        </el-button>

        <el-button
          type="default"
          size="small"
          @click.stop="handleComment"
          class="action-btn"
        >
          <svg class="icon" viewBox="0 0 1024 1024" width="16" height="16">
            <path d="M573 421c-23.1 0-41 17.9-41 40s17.9 40 41 40c21.1 0 39-17.9 39-40s-17.9-40-39-40zm-280 0c-23.1 0-41 17.9-41 40s17.9 40 41 40c21.1 0 39-17.9 39-40s-17.9-40-39-40z" fill="currentColor"/>
            <path d="M894 345c-48.1-66-115.3-110.1-189-130v.1c-17.1-19-36.4-36.5-58-52.1-163.7-119-393.5-82.7-513 81-96.3 133-92.2 311.9 6 439l.8 132.6c0 3.2.5 6.4 1.5 9.4 5.3 16.9 23.3 26.2 40.1 20.9L309 806c33.5 11.9 68.1 18.7 102.5 20.6l-.5.4c89.1 64.9 205.9 84.4 313 49l127.1 41.4c3.2 1 6.5 1.6 9.9 1.6 17.7 0 32-14.3 32-32V753.8c88.1-119.6 90.4-284.9 1-408.8zM323 735l-12-5-99 31-1-104-8-9c-84.6-103.2-90.2-251.9-11-361 96.4-132.2 281.2-161.4 413-66 132.2 96.1 161.5 280.6 66 412-80.1 109.9-223.5 150.5-348 102z" fill="currentColor"/>
          </svg>
          {{ post.commentCount || 0 }}
        </el-button>

        <el-button
          :type="post.hasFavorited ? 'warning' : 'default'"
          size="small"
          @click.stop="handleFavorite"
          class="action-btn"
        >
          <svg class="icon" viewBox="0 0 1024 1024" width="16" height="16">
            <path d="M908.1 353.1l-253.9-36.9L540.7 86.1c-3.1-6.3-8.2-11.4-14.5-14.5-15.8-7.8-35-1.3-42.9 14.5L369.8 316.2l-253.9 36.9c-7 1-13.4 4.3-18.3 9.3a32.05 32.05 0 0 0 .6 45.3l183.7 179.1-43.4 252.9a31.95 31.95 0 0 0 46.4 33.7L512 754l227.1 119.4c6.2 3.3 13.4 4.4 20.3 3.2 17.4-3 29.1-19.5 26.1-36.9l-43.4-252.9 183.7-179.1c5-4.9 8.3-11.3 9.3-18.3 2.7-17.5-9.5-33.7-27-36.3z" fill="currentColor"/>
          </svg>
          {{ post.hasFavorited ? '已收藏' : '收藏' }}
        </el-button>
      </div>

      <div class="post-stats">
        <span class="view-count">阅读 {{ post.viewCount || 0 }}</span>
      </div>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

interface Props {
  post: API.PostVO
}

const props = defineProps<Props>()
const router = useRouter()

// 格式化时间
const formatTime = (time: string | undefined) => {
  if (!time) return '刚刚'
  try {
    const date = new Date(time)
    const now = new Date()
    const diff = now.getTime() - date.getTime()

    const minutes = Math.floor(diff / (1000 * 60))
    const hours = Math.floor(diff / (1000 * 60 * 60))
    const days = Math.floor(diff / (1000 * 60 * 60 * 24))

    if (minutes < 1) return '刚刚'
    if (minutes < 60) return `${minutes}分钟前`
    if (hours < 24) return `${hours}小时前`
    if (days < 7) return `${days}天前`

    return date.toLocaleDateString('zh-CN')
  } catch {
    return '刚刚'
  }
}

// 解析标签
const parseTags = (tags: string | string[]) => {
  if (!tags) return []
  
  // 如果是数组格式，直接返回
  if (Array.isArray(tags)) {
    return tags.filter(tag => tag && tag.trim())
  }
  
  // 如果是字符串格式
  if (typeof tags === 'string') {
    try {
      // 尝试解析JSON格式
      const parsed = JSON.parse(tags)
      if (Array.isArray(parsed)) {
        return parsed.filter(tag => tag && tag.trim())
      }
    } catch {
      // 如果不是JSON格式，按逗号分割
      return tags.split(',').map(tag => tag.trim()).filter(tag => tag)
    }
  }
  
  return []
}

const emit = defineEmits<{
  like: [postId: number]
  favorite: [postId: number]
}>()

// 跳转到帖子详情页
const goToDetail = () => {
  router.push(`/community/post/${props.post.id}`)
}

// 处理点赞
const handleLike = () => {
  if (props.post.id) {
    emit('like', props.post.id)
  }
}

// 处理收藏
const handleFavorite = () => {
  if (props.post.id) {
    emit('favorite', props.post.id)
  }
}

// 处理评论 - 跳转到帖子详情页
const handleComment = () => {
  if (props.post.id) {
    router.push(`/community/post/${props.post.id}`)
  }
}
</script>

<style scoped>
.post-card {
  margin-bottom: 16px;
  transition: all 0.3s ease;
}

.post-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.post-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-avatar {
  flex-shrink: 0;
}

.user-details {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.username {
  font-weight: 500;
  color: #333;
  font-size: 14px;
}

.post-time {
  font-size: 12px;
  color: #999;
}

.post-meta {
  display: flex;
  align-items: center;
  gap: 8px;
}

.essence-tag {
  font-size: 10px;
}

.post-content {
  margin-bottom: 16px;
}

.post-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 8px;
  line-height: 1.4;
}

.post-text {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  white-space: pre-wrap;
  word-break: break-word;
}

.post-tags {
  margin-top: 12px;
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.tag-item {
  font-size: 11px;
  border-radius: 12px;
}

.post-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 12px;
  border-top: 1px solid #f0f0f0;
}

.action-group {
  display: flex;
  gap: 8px;
}

.action-btn {
  border-radius: 16px;
  font-size: 12px;
  padding: 4px 12px;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  gap: 4px;
}

.action-btn .icon {
  flex-shrink: 0;
}

.action-btn:hover {
  transform: scale(1.05);
}

.post-stats {
  display: flex;
  align-items: center;
  gap: 12px;
}

.view-count {
  font-size: 12px;
  color: #999;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .post-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }

  .post-meta {
    align-self: flex-end;
  }

  .post-actions {
    flex-direction: column;
    gap: 8px;
    align-items: stretch;
  }

  .action-group {
    justify-content: center;
  }

  .post-stats {
    justify-content: center;
  }
}
</style>
