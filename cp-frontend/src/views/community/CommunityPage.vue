<template>
  <div class="community-container">
    <!-- 发布帖子卡片 -->
    <el-card class="publish-card" shadow="hover">
      <div class="publish-content">
        <el-input
          v-model="postForm.content"
          type="textarea"
          :rows="4"
          placeholder="分享你的想法..."
          maxlength="1000"
          show-word-limit
          class="content-input"
        />
        <div class="publish-footer">
          <div class="publish-left">
            <div class="tags-section">
          <div class="tags-label">添加标签：</div>
          <el-input-tag
            v-model="selectedTags"
            placeholder="输入标签后按回车添加"
            class="tags-input"
          />
        </div>
          </div>
          <div class="publish-right">
            <el-button
              type="primary"
              @click="publishPost"
              :loading="publishing"
              :disabled="!postForm.content.trim()"
            >
              发布
            </el-button>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 标签页切换 -->
    <el-tabs v-model="activeTab" @tab-change="handleTabChange" class="content-tabs">
      <el-tab-pane label="最新帖子" name="latest">
        <PostList
          :posts="posts"
          :loading="loading"
          @like="handleLike"
          @favorite="handleFavorite"
          @comment="handleComment"
          @load-more="loadMorePosts"
        />
      </el-tab-pane>
      <el-tab-pane label="精选内容" name="essence">
        <PostList
          :posts="posts"
          :loading="loading"
          @like="handleLike"
          @favorite="handleFavorite"
          @comment="handleComment"
          @load-more="loadMorePosts"
        />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { addPost, queryPostPage, toggleLike, toggleFavorite } from '@/api/postController'
import PostList from './components/PostList.vue'

// 响应式数据
const activeTab = ref('latest')
const publishing = ref(false)
const loading = ref(false)
const posts = ref<API.PostVO[]>([])
const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0
})

// 发布帖子表单
const postForm = reactive({
  title: '',
  content: '',
  tags: ''
})

const selectedTags = ref<string[]>([])

// 发布帖子
const publishPost = async () => {
  if (!postForm.content.trim()) {
    ElMessage.warning('请输入帖子内容')
    return
  }

  try {
    publishing.value = true
    
    // 处理标签格式：使用选中的标签
    const tagsArray = selectedTags.value
    
    const request: API.PostAddRequest = {
      title: postForm.content.substring(0, 50) || '无标题', // 使用内容前50字符作为标题
      content: postForm.content,
      tags: JSON.stringify(tagsArray) // 转换为JSON字符串格式
    }

    const res = await addPost(request)
    if (res.data.code === 200) {
      ElMessage.success('发布成功！')
      // 清空表单
      postForm.content = ''
      postForm.tags = ''
      selectedTags.value = []
      // 重新加载帖子列表
      await loadPosts(true)
    } else {
      ElMessage.error(res.data.message || '发布失败')
    }
  } catch (error) {
    console.error('发布帖子失败:', error)
    ElMessage.error('发布失败')
  } finally {
    publishing.value = false
  }
}

// 加载帖子列表
const loadPosts = async (reset = false) => {
  try {
    loading.value = true
    
    if (reset) {
      pagination.value.current = 1
      posts.value = []
    }

    const queryParams: API.PostQueryRequest = {
      current: pagination.value.current,
      pageSize: pagination.value.pageSize,
      sortField: 'createdAt',
      sortOrder: 'desc'
    }

    // 根据当前标签页设置查询条件
    if (activeTab.value === 'essence') {
      queryParams.isEssence = 1
    }

    const res = await queryPostPage(queryParams)
    if (res.data.code === 200 && res.data.data) {
      const newPosts = res.data.data.records || []
      if (reset) {
        posts.value = newPosts
      } else {
        posts.value.push(...newPosts)
      }
      pagination.value.total = res.data.data.totalRow || 0
    } else {
      ElMessage.error(res.data.message || '加载帖子失败')
    }
  } catch (error) {
    console.error('加载帖子失败:', error)
    ElMessage.error('加载帖子失败')
  } finally {
    loading.value = false
  }
}

// 加载更多帖子
const loadMorePosts = async () => {
  if (posts.value.length >= pagination.value.total) {
    return
  }
  pagination.value.current++
  await loadPosts(false)
}

// 标签页切换
const handleTabChange = async (tabName: string) => {
  activeTab.value = tabName
  await loadPosts(true)
}

// 点赞处理
const handleLike = async (postId: number) => {
  try {
    const res = await toggleLike({ postId })
    if (res.data.code === 200) {
      // 更新帖子的点赞状态
      const post = posts.value.find(p => p.id === postId)
      if (post) {
        post.hasLiked = !post.hasLiked
        if (post.hasLiked) {
          post.likeCount = (post.likeCount || 0) + 1
        } else {
          post.likeCount = Math.max((post.likeCount || 0) - 1, 0)
        }
      }
    } else {
      ElMessage.error(res.data.message || '操作失败')
    }
  } catch (error) {
    console.error('点赞操作失败:', error)
    ElMessage.error('操作失败')
  }
}

// 收藏处理
const handleFavorite = async (postId: number) => {
  try {
    const res = await toggleFavorite({ postId })
    if (res.data.code === 200) {
      // 更新帖子的收藏状态
      const post = posts.value.find(p => p.id === postId)
      if (post) {
        post.hasFavorited = !post.hasFavorited
      }
      ElMessage.success(post?.hasFavorited ? '已收藏' : '已取消收藏')
    } else {
      ElMessage.error(res.data.message || '操作失败')
    }
  } catch (error) {
    console.error('收藏操作失败:', error)
    ElMessage.error('操作失败')
  }
}

// 评论处理（暂时只是提示）
const handleComment = (postId: number) => {
  ElMessage.info('评论功能开发中...')
}

// 页面加载时获取帖子列表
onMounted(() => {
  loadPosts(true)
})
</script>

<style scoped>
.community-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.publish-card {
  margin-bottom: 20px;
}

.publish-content {
  padding: 10px;
}

.content-input {
  margin-bottom: 15px;
}

.publish-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 15px;
}

.publish-left {
  flex: 1;
}

.tags-section {
  max-width: 300px;
}

.tags-label {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}

.tags-input {
  width: 100%;
}

.publish-right {
  flex-shrink: 0;
}

.content-tabs {
  margin-top: 20px;
}

:deep(.el-tabs__header) {
  margin-bottom: 20px;
}

:deep(.el-tabs__nav-wrap) {
  padding: 0 10px;
}
</style>