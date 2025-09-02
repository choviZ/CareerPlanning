<template>
  <div class="post-detail-page">
    <!-- 帖子详情卡片 -->
    <div class="post-detail-card">
      <div class="post-header">
        <div class="user-info">
          <el-avatar :size="40" :src="post.user?.userAvatar" />
          <div class="user-details">
            <div class="username">{{ post.user?.userName }}</div>
            <div class="post-time">{{ formatTime(post.createdAt) }}</div>
          </div>
        </div>
        <el-button type="primary" size="small" class="follow-btn">
          + 关注
        </el-button>
      </div>

      <div class="post-content">
        <h2 class="post-title">{{ post.title }}</h2>
        <div class="post-text">{{ post.content }}</div>

        <!-- 标签 -->
        <div class="post-tags" v-if="post.tagList && post.tagList.length > 0">
          <el-tag
            v-for="tag in parseTags(post.tagList)"
            :key="tag"
            size="small"
            class="tag-item"
          >
            {{ tag }}
          </el-tag>
        </div>
      </div>

      <!-- 操作按钮 -->
      <div class="post-actions">
        <el-button
          :type="post.hasLiked ? 'primary' : 'default'"
          size="small"
          @click="handleLike"
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
          class="action-btn"
        >
          <svg class="icon" viewBox="0 0 1024 1024" width="16" height="16">
            <path d="M573 421c-23.1 0-41 17.9-41 40s17.9 40 41 40c21.1 0 39-17.9 39-40s-17.9-40-39-40zm-280 0c-23.1 0-41 17.9-41 40s17.9 40 41 40c21.1 0 39-17.9 39-40s-17.9-40-39-40z" fill="currentColor"/>
            <path d="M894 345c-48.1-66-115.3-110.1-189-130v.1c-17.1-19-36.4-36.5-58-52.1-163.7-119-393.5-82.7-513 81-96.3 133-92.2 311.9 6 439l.8 132.6c0 3.2.5 6.4 1.5 9.4 5.3 16.9 23.3 26.2 40.1 20.9L309 806c33.5 11.9 68.1 18.7 102.5 20.6l-.5.4c89.1 64.9 205.9 84.4 313 49l127.1 41.4c3.2 1 6.5 1.6 9.9 1.6 17.7 0 32-14.3 32-32V753.8c88.1-119.6 90.4-284.9 1-408.8zM323 735l-12-5-99 31-1-104-8-9c-84.6-103.2-90.2-251.9-11-361 96.4-132.2 281.2-161.4 413-66 132.2 96.1 161.5 280.6 66 412-80.1 109.9-223.5 150.5-348 102z" fill="currentColor"/>
          </svg>
          {{ comments.length }}个评论
        </el-button>

        <el-button
          :type="post.hasFavorited ? 'warning' : 'default'"
          size="small"
          @click="handleFavorite"
          class="action-btn"
        >
          <svg class="icon" viewBox="0 0 1024 1024" width="16" height="16">
            <path d="M908.1 353.1l-253.9-36.9L540.7 86.1c-3.1-6.3-8.2-11.4-14.5-14.5-15.8-7.8-35-1.3-42.9 14.5L369.8 316.2l-253.9 36.9c-7 1-13.4 4.3-18.3 9.3a32.05 32.05 0 0 0 .6 45.3l183.7 179.1-43.4 252.9a31.95 31.95 0 0 0 46.4 33.7L512 754l227.1 119.4c6.2 3.3 13.4 4.4 20.3 3.2 17.4-3 29.1-19.5 26.1-36.9l-43.4-252.9 183.7-179.1c5-4.9 8.3-11.3 9.3-18.3 2.7-17.5-9.5-33.7-27-36.3z" fill="currentColor"/>
          </svg>
          分享
        </el-button>
      </div>
    </div>

    <!-- 评论区 -->
    <div class="comments-section">
      <!-- 评论标题和排序 -->
      <div class="comments-header">
        <div class="comments-title">{{ comments.length }}个评论</div>
        <div class="comment-sort">
          <el-button
            :type="sortType === 'hot' ? 'primary' : 'default'"
            size="small"
            @click="sortType = 'hot'"
          >
            最热
          </el-button>
          <el-button
            :type="sortType === 'latest' ? 'primary' : 'default'"
            size="small"
            @click="sortType = 'latest'"
          >
            最新
          </el-button>
          <el-button
            :type="sortType === 'floor' ? 'primary' : 'default'"
            size="small"
            @click="sortType = 'floor'"
          >
            楼层
          </el-button>
        </div>
      </div>

      <!-- 发表评论 -->
      <div class="comment-input-section">
        <el-avatar :size="32" class="comment-avatar" />
        <div class="comment-input-wrapper">
          <el-input
            v-model="newComment"
            type="textarea"
            :rows="3"
            placeholder="快来和大家讨论吧～"
            class="comment-input"
          />
          <div class="comment-actions">
            <div class="comment-tools">
              <el-button text size="small">
                <svg class="icon" viewBox="0 0 1024 1024" width="16" height="16">
                  <path d="M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64z" fill="currentColor"/>
                </svg>
                表情
              </el-button>
              <el-button text size="small">
                <svg class="icon" viewBox="0 0 1024 1024" width="16" height="16">
                  <path d="M928 160H96c-17.7 0-32 14.3-32 32v640c0 17.7 14.3 32 32 32h832c17.7 0 32-14.3 32-32V192c0-17.7-14.3-32-32-32z" fill="currentColor"/>
                </svg>
                图片
              </el-button>
              <el-button text size="small">
                <svg class="icon" viewBox="0 0 1024 1024" width="16" height="16">
                  <path d="M854.6 288.6L639.4 73.4c-6-6-14.1-9.4-22.6-9.4H192c-17.7 0-32 14.3-32 32v832c0 17.7 14.3 32 32 32h640c17.7 0 32-14.3 32-32V311.3c0-8.5-3.4-16.7-9.4-22.7z" fill="currentColor"/>
                </svg>
                切换
              </el-button>
            </div>
            <el-button type="primary" size="small" @click="submitComment">
              发布
            </el-button>
          </div>
        </div>
      </div>

      <!-- 评论列表 -->
      <div class="comments-list">
        <div
          v-for="comment in sortedComments"
          :key="comment.id"
          class="comment-item"
        >
          <el-avatar :size="32" :src="comment.user?.userAvatar" class="comment-avatar" />
          <div class="comment-content">
            <div class="comment-header">
              <span class="comment-username">{{ comment.user?.userName }}</span>
              <span class="comment-time">{{ formatTime(comment.createdAt) }}</span>
            </div>
            <div class="comment-text">{{ comment.content }}</div>
            <div class="comment-actions">
              <el-button
                text
                size="small"
                @click="likeComment(comment)"
                :class="{ 'liked': comment.hasLiked }"
              >
                <svg class="icon" viewBox="0 0 1024 1024" width="14" height="14">
                  <path d="M885.9 533.7c16.8-22.2 26.1-49.4 26.1-77.7 0-44.9-25.1-87.4-65.5-111.1a67.67 67.67 0 0 0-34.3-9.3H572.4l6-122.9c1.4-29.7-9.1-57.9-29.5-79.4A106.62 106.62 0 0 0 471 99.9c-52 0-98 35-111.8 85.1l-85.9 311H144c-17.7 0-32 14.3-32 32v364c0 17.7 14.3 32 32 32h601.3c9.2 0 18.2-1.8 26.8-5.4 99.3-41.3 180.7-131 180.7-235.1 0-52.7-19.7-101.9-55.9-139.5z" fill="currentColor"/>
                </svg>
                {{ comment.likeCount || 0 }}
              </el-button>
              <el-button
                text
                size="small"
                @click="replyToComment(comment)"
                :type="replyingTo === comment.id ? 'primary' : 'default'"
              >
                {{ replyingTo === comment.id ? '取消回复' : '回复' }}
              </el-button>
            </div>

            <!-- 回复输入框 -->
            <div v-if="replyingTo === comment.id" class="reply-input-section">
              <el-avatar :size="24" class="reply-avatar" />
              <div class="reply-input-wrapper">
                <el-input
                  v-model="replyContent"
                  type="textarea"
                  :rows="2"
                  :placeholder="`回复 ${comment.user?.userName}:`"
                  class="reply-input"
                />
                <div class="reply-actions">
                  <el-button size="small" @click="cancelReply">取消</el-button>
                  <el-button
                    type="primary"
                    size="small"
                    @click="submitReply(comment.id!)"
                    :disabled="!replyContent.trim()"
                  >
                    回复
                  </el-button>
                </div>
              </div>
            </div>

            <!-- 回复列表 -->
            <div v-if="comment.replyCount && comment.replyCount > 0" class="replies-list">
              <!-- 未加载回复时显示查看按钮 -->
              <div v-if="!(comment as any).showReplies" class="reply-placeholder">
                <span class="reply-count">{{ comment.replyCount }}条回复</span>
                <el-button text size="small" @click="loadReplies(comment.id)">
                  查看回复
                </el-button>
              </div>
              
              <!-- 已加载回复时显示回复列表 -->
              <div v-else class="replies-container">
                <div class="replies-header">
                  <span class="reply-count">{{ comment.replyCount }}条回复</span>
                  <el-button text size="small" @click="(comment as any).showReplies = false">
                    收起回复
                  </el-button>
                </div>
                
                <div class="reply-item" v-for="reply in (comment as any).replies" :key="reply.id">
                   <el-avatar :size="32" :src="reply.user?.userAvatar" class="reply-avatar" />
                   <div class="reply-content">
                     <div class="reply-header">
                       <span class="reply-username">{{ reply.user?.userName }}</span>
                       <span v-if="reply.replyToUser" class="reply-to">
                         回复 @{{ reply.replyToUser.userName }}
                       </span>
                       <span class="reply-time">{{ formatTime(reply.createdAt) }}</span>
                     </div>
                     <div class="reply-text">{{ reply.content }}</div>
                     <div class="reply-actions">
                       <el-button
                         text
                         size="small"
                         @click="likeComment(reply)"
                         :class="{ 'liked': reply.hasLiked }"
                       >
                         <svg class="icon" viewBox="0 0 1024 1024" width="14" height="14">
                           <path d="M885.9 533.7c16.8-22.2 26.1-49.4 26.1-77.7 0-44.9-25.1-87.4-65.5-111.1a67.67 67.67 0 0 0-34.3-9.3H572.4l6-122.9c1.4-29.7-9.1-57.9-29.5-79.4A106.62 106.62 0 0 0 471 99.9c-52 0-98 35-111.8 85.1l-85.9 311H144c-17.7 0-32 14.3-32 32v364c0 17.7 14.3 32 32 32h601.3c9.2 0 18.2-1.8 26.8-5.4 99.3-41.3 180.7-131 180.7-235.1 0-52.7-19.7-101.9-55.9-139.5z" fill="currentColor"/>
                         </svg>
                         {{ reply.likeCount || 0 }}
                       </el-button>
                       <el-button text size="small" @click="replyToComment(reply)">
                         {{ replyingTo === reply.id ? '取消回复' : '回复' }}
                       </el-button>
                     </div>
                     
                     <!-- 子评论的回复输入框 -->
                      <div v-if="replyingTo === reply.id" class="reply-input-section">
                        <el-avatar :size="24" class="reply-avatar" />
                        <div class="reply-input-wrapper">
                          <el-input
                            v-model="replyContent"
                            type="textarea"
                            :rows="2"
                            :placeholder="`回复 ${reply.user?.userName}:`"
                            class="reply-input"
                          />
                          <div class="reply-actions">
                            <el-button size="small" @click="cancelReply">取消</el-button>
                            <el-button
                              type="primary"
                              size="small"
                              @click="submitReply(comment.id!, reply.id, reply.user?.id)"
                              :disabled="!replyContent.trim()"
                            >
                              回复
                            </el-button>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getPostById, toggleLike, toggleFavorite } from '@/api/postController.ts'
import {
  listCommentByPage,
  addComment,
  likeComment as likeCommentApi,
  getPostComments,
  getCommentReplies
} from '@/api/commentController.ts'

const route = useRoute()
const postId = route.params.id as string

// 帖子数据
const post = ref<API.PostVO>({})
const loading = ref(false)

// 评论数据
const comments = ref<API.CommentVO[]>([])
const newComment = ref('')
const sortType = ref<'hot' | 'latest' | 'floor'>('hot')

// 回复相关状态
const replyingTo = ref<number | null>(null)
const replyContent = ref('')

// 获取帖子详情
const loadPostDetail = async () => {
  try {
    loading.value = true
    const response = await getPostById({ postId: parseInt(postId) })
    if (response.data?.data) {
      post.value = response.data.data
    }
  } catch (error) {
    console.error('获取帖子详情失败:', error)
    ElMessage.error('获取帖子详情失败')
  } finally {
    loading.value = false
  }
}

// 获取评论列表
const loadComments = async () => {
  try {
    const response = await getPostComments({ postId: parseInt(postId) })
    if (response.data?.data) {
      comments.value = response.data.data
    }
  } catch (error) {
    console.error('获取评论失败:', error)
    ElMessage.error('获取评论失败')
  }
}

// 排序后的评论
const sortedComments = computed(() => {
  const commentsCopy = [...comments.value]
  switch (sortType.value) {
    case 'hot':
      return commentsCopy.sort((a, b) => (b.likeCount || 0) - (a.likeCount || 0))
    case 'latest':
      return commentsCopy.sort((a, b) => {
        const timeA = a.createdAt ? new Date(a.createdAt).getTime() : 0
        const timeB = b.createdAt ? new Date(b.createdAt).getTime() : 0
        return timeB - timeA
      })
    case 'floor':
      return commentsCopy.sort((a, b) => {
        const timeA = a.createdAt ? new Date(a.createdAt).getTime() : 0
        const timeB = b.createdAt ? new Date(b.createdAt).getTime() : 0
        return timeA - timeB
      })
    default:
      return commentsCopy
  }
})

// 时间格式化
const formatTime = (time: string | undefined) => {
  if (!time) return ''
  const now = new Date()
  const postTime = new Date(time)
  const diff = now.getTime() - postTime.getTime()

  const minutes = Math.floor(diff / (1000 * 60))
  const hours = Math.floor(diff / (1000 * 60 * 60))
  const days = Math.floor(diff / (1000 * 60 * 60 * 24))

  if (minutes < 1) return '刚刚'
  if (minutes < 60) return `${minutes}分钟前`
  if (hours < 24) return `${hours}小时前`
  if (days < 7) return `${days}天前`

  return postTime.toLocaleDateString()
}

// 解析标签
const parseTags = (tags: any): string[] => {
  if (!tags) return []

  // 如果已经是数组，直接返回
  if (Array.isArray(tags)) {
    return tags
  }

  // 如果是字符串，尝试解析
  if (typeof tags === 'string') {
    try {
      // 尝试解析JSON格式
      const parsed = JSON.parse(tags)
      if (Array.isArray(parsed)) {
        return parsed
      }
    } catch {
      // 如果不是JSON，按逗号分割
      return tags.split(',').map(tag => tag.trim()).filter(tag => tag)
    }
  }

  return []
}

// 点赞帖子
const handleLike = async () => {
  try {
    if (!post.value.id) return

    const response = await toggleLike({ postId: post.value.id })

    if (response.data?.code === 200) {
      // 重新加载帖子详情以获取最新状态
      await loadPostDetail()
      ElMessage.success(post.value.hasLiked ? '取消点赞' : '点赞成功')
    } else {
      ElMessage.error(response.data?.message || '操作失败')
    }
  } catch (error) {
    console.error('点赞操作失败:', error)
    ElMessage.error('操作失败')
  }
}

// 收藏帖子
const handleFavorite = async () => {
  try {
    if (!post.value.id) return

    const response = await toggleFavorite({ postId: post.value.id })

    if (response.data?.code === 200) {
      // 重新加载帖子详情以获取最新状态
      await loadPostDetail()
      ElMessage.success(post.value.hasFavorited ? '取消收藏' : '收藏成功')
    } else {
      ElMessage.error(response.data?.message || '操作失败')
    }
  } catch (error) {
    console.error('收藏操作失败:', error)
    ElMessage.error('操作失败')
  }
}

// 发表评论
const submitComment = async () => {
  if (!newComment.value.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }

  try {
    const response = await addComment({
      content: newComment.value,
      postId: parseInt(postId)
    })

    if (response.data?.code === 200) {
      newComment.value = ''
      ElMessage.success('评论发表成功')
      // 重新加载评论列表
      await loadComments()
    } else {
      ElMessage.error(response.data?.message || '评论发表失败')
    }
  } catch (error) {
    console.error('评论发表失败:', error)
    ElMessage.error('评论发表失败')
  }
}

// 点赞评论
const likeComment = async (comment: API.CommentVO) => {
  try {
    if (!comment.id) return

    const response = await likeCommentApi({ commentId: comment.id })

    if (response.data?.code === 200) {
      // 更新本地状态
      comment.hasLiked = !comment.hasLiked
      comment.likeCount = (comment.likeCount || 0) + (comment.hasLiked ? 1 : -1)
      
      ElMessage.success(comment.hasLiked ? '点赞成功' : '取消点赞')
    } else {
      ElMessage.error(response.data?.message || '操作失败')
    }
  } catch (error) {
    console.error('点赞操作失败:', error)
    ElMessage.error('操作失败')
  }
}

// 回复评论
const replyToComment = (comment: API.CommentVO) => {
  if (replyingTo.value === comment.id) {
    // 如果已经在回复这条评论，则取消回复
    replyingTo.value = null
    replyContent.value = ''
  } else {
    // 开始回复这条评论
    replyingTo.value = comment.id || null
    replyContent.value = ''
  }
}

// 提交回复
const submitReply = async (parentCommentId: number, replyToCommentId?: number, replyToUserId?: number) => {
  if (!replyContent.value.trim()) {
    ElMessage.warning('请输入回复内容')
    return
  }

  try {
    const requestData: any = {
      content: replyContent.value,
      postId: parseInt(postId),
      parentId: parentCommentId
    }

    // 如果是回复子评论，添加 replyToUserId
    if (replyToCommentId && replyToUserId) {
      requestData.replyToUserId = replyToUserId
    }

    const response = await addComment(requestData)

    if (response.data?.code === 200) {
      replyContent.value = ''
      replyingTo.value = null
      ElMessage.success('回复发表成功')
      // 重新加载评论列表
      await loadComments()
    } else {
      ElMessage.error(response.data?.message || '回复发表失败')
    }
  } catch (error) {
    console.error('回复发表失败:', error)
    ElMessage.error('回复发表失败')
  }
}

// 取消回复
const cancelReply = () => {
  replyingTo.value = null
  replyContent.value = ''
}

// 加载回复
const loadReplies = async (commentId: number | undefined) => {
  if (!commentId) return

  try {
    const response = await getCommentReplies({ commentId })
    if (response.data?.data) {
      // 找到对应的评论并添加回复数据
      const targetComment = comments.value.find(comment => comment.id === commentId)
      if (targetComment) {
        // 为评论对象动态添加 replies 字段
        ;(targetComment as any).replies = response.data.data
        ;(targetComment as any).showReplies = true
        ElMessage.success(`加载了 ${response.data.data.length} 条回复`)
      }
    }
  } catch (error) {
    console.error('加载回复失败:', error)
    ElMessage.error('加载回复失败')
  }
}

onMounted(() => {
  loadPostDetail()
  loadComments()
})
</script>

<style scoped>
.post-detail-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  background: #f5f5f5;
  min-height: 100vh;
}

/* 帖子详情卡片 */
.post-detail-card {
  background: white;
  border-radius: 8px;
  padding: 24px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.post-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-details {
  display: flex;
  flex-direction: column;
}

.username {
  font-weight: 600;
  color: #333;
  font-size: 16px;
}

.post-time {
  color: #999;
  font-size: 14px;
  margin-top: 2px;
}

.follow-btn {
  border-radius: 20px;
}

.post-content {
  margin-bottom: 20px;
}

.post-title {
  font-size: 24px;
  font-weight: 600;
  color: #333;
  margin: 0 0 16px 0;
  line-height: 1.4;
}

.post-text {
  font-size: 16px;
  line-height: 1.6;
  color: #333;
  margin-bottom: 16px;
  white-space: pre-wrap;
}

.post-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tag-item {
  background: #f0f9ff;
  color: #0369a1;
  border: 1px solid #bae6fd;
}

.post-actions {
  display: flex;
  gap: 12px;
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 4px;
  border-radius: 20px;
  padding: 8px 16px;
  transition: all 0.2s ease;
}

.action-btn .icon {
  flex-shrink: 0;
}

/* 评论区 */
.comments-section {
  background: white;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.comments-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f0f0f0;
}

.comments-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.comment-sort {
  display: flex;
  gap: 8px;
}

.comment-sort .el-button {
  border-radius: 20px;
}

/* 发表评论 */
.comment-input-section {
  display: flex;
  gap: 12px;
  margin-bottom: 24px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.comment-input-wrapper {
  flex: 1;
}

.comment-input {
  margin-bottom: 12px;
}

.comment-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.comment-tools {
  display: flex;
  gap: 8px;
}

.comment-tools .el-button {
  color: #666;
  padding: 4px 8px;
}

.comment-tools .icon {
  margin-right: 4px;
}

/* 评论列表 */
.comments-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.comment-item {
  display: flex;
  gap: 12px;
}

.comment-content {
  flex: 1;
}

.comment-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}

.comment-username {
  font-weight: 600;
  color: #333;
  font-size: 14px;
}

.comment-time {
  color: #999;
  font-size: 12px;
}

.comment-text {
  color: #333;
  line-height: 1.5;
  margin-bottom: 8px;
}

.comment-actions {
  display: flex;
  gap: 16px;
}

.comment-actions .el-button {
  color: #666;
  padding: 2px 8px;
  font-size: 12px;
}

.comment-actions .el-button.liked {
  color: #409eff;
}

.comment-actions .icon {
  margin-right: 4px;
}

/* 回复列表 */
.replies-list {
  margin-top: 12px;
  border-left: 2px solid #e9ecef;
  padding-left: 16px;
}

.reply-placeholder {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  color: #666;
  font-size: 13px;
}

.replies-container {
  margin-top: 8px;
}

.replies-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  color: #666;
  font-size: 13px;
  border-bottom: 1px solid #f0f0f0;
  margin-bottom: 12px;
}

.reply-item {
  display: flex;
  gap: 8px;
  margin-bottom: 12px;
  padding: 8px;
  background: #fafafa;
  border-radius: 6px;
}

.reply-avatar {
  flex-shrink: 0;
}

.reply-content {
  flex: 1;
  min-width: 0;
}

.reply-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 4px;
}

.reply-username {
  font-weight: 500;
  color: #333;
  font-size: 13px;
}

.reply-to {
  color: #409eff;
  font-size: 12px;
}

.reply-time {
  color: #999;
  font-size: 11px;
  margin-left: auto;
}

.reply-text {
  color: #333;
  line-height: 1.4;
  margin-bottom: 6px;
  font-size: 13px;
}

.reply-actions {
  display: flex;
  gap: 12px;
}

.reply-actions .el-button {
  color: #666;
  padding: 1px 6px;
  font-size: 11px;
}

.reply-actions .el-button.liked {
  color: #409eff;
}

.reply-actions .icon {
  margin-right: 3px;
}

/* 回复输入框 */
.reply-input-section {
  margin-top: 12px;
  display: flex;
  gap: 8px;
  padding: 12px;
  background: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e9ecef;
}

.reply-avatar {
  flex-shrink: 0;
}

.reply-input-wrapper {
  flex: 1;
}

.reply-input {
  margin-bottom: 8px;
}

.reply-input .el-textarea__inner {
  border: 1px solid #dcdfe6;
  border-radius: 6px;
  font-size: 14px;
}

.reply-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}

/* 回复列表 */
.replies-list {
  margin-top: 12px;
  padding-left: 20px;
  border-left: 2px solid #f0f0f0;
}

.reply-placeholder {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 12px;
  background: #f8f9fa;
  border-radius: 6px;
  font-size: 13px;
}

.reply-count {
  color: #666;
}
</style>
