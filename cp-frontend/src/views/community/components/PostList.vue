<template>
  <div class="post-list">
    <div v-if="loading && posts.length === 0" class="loading-container">
      <el-skeleton :rows="3" animated />
      <el-skeleton :rows="3" animated />
      <el-skeleton :rows="3" animated />
    </div>
    
    <div v-else-if="posts.length === 0" class="empty-container">
      <el-empty description="暂无帖子" />
    </div>
    
    <div v-else>
      <PostItem
        v-for="post in posts"
        :key="post.id"
        :post="post"
        @like="$emit('like', $event)"
        @favorite="$emit('favorite', $event)"

        class="post-item"
      />
      
      <!-- 加载更多按钮 -->
      <div v-if="hasMore" class="load-more-container">
        <el-button
          @click="$emit('load-more')"
          :loading="loading"
          type="text"
          class="load-more-btn"
        >
          {{ loading ? '加载中...' : '加载更多' }}
        </el-button>
      </div>
      
      <div v-else-if="posts.length > 0" class="no-more-tip">
        <el-divider>没有更多内容了</el-divider>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import PostItem from './PostItem.vue'

interface Props {
  posts: API.PostVO[]
  loading: boolean
  total?: number
}

const props = withDefaults(defineProps<Props>(), {
  total: 0
})

defineEmits<{
  like: [postId: number]
  favorite: [postId: number]
  'load-more': []
}>()

// 是否还有更多数据
const hasMore = computed(() => {
  return props.posts.length < props.total
})
</script>

<style scoped>
.post-list {
  min-height: 200px;
}

.loading-container {
  padding: 20px;
}

.empty-container {
  padding: 40px 20px;
  text-align: center;
}

.post-item {
  margin-bottom: 20px;
}

.load-more-container {
  text-align: center;
  padding: 20px;
}

.load-more-btn {
  font-size: 14px;
  color: #409eff;
}

.load-more-btn:hover {
  color: #66b1ff;
}

.no-more-tip {
  margin-top: 20px;
}

:deep(.el-divider__text) {
  color: #999;
  font-size: 12px;
}
</style>