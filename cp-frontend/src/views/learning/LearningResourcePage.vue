<template>
  <div class="learning-resource-container">
    <!-- 页面标题和搜索区域 -->
    <el-card class="header-card" shadow="hover">
      <div class="header-content">
        <div class="title-section">
          <h2 class="page-title">学习资源</h2>
          <p class="page-subtitle">发现优质学习内容，提升职业技能</p>
        </div>
        <div class="search-section">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索学习资源..."
            class="search-input"
            clearable
            @keyup.enter="handleSearch"
          >
            <template #append>
              <el-button @click="handleSearch" :loading="loading">
                <el-icon>
                  <Search />
                </el-icon>
              </el-button>
            </template>
          </el-input>
        </div>
      </div>
    </el-card>

    <!-- 分类筛选 -->
    <el-card class="filter-card" shadow="hover">
      <div class="filter-content">
        <div class="filter-item">
          <span class="filter-label">分类：</span>
          <el-radio-group v-model="selectedCategory" @change="handleCategoryChange">
            <el-radio-button label="">全部</el-radio-button>
            <el-radio-button label="计算机">计算机</el-radio-button>
          </el-radio-group>
        </div>
        <div class="filter-item">
          <span class="filter-label">类型：</span>
          <el-radio-group v-model="selectedResourceType" @change="handleTypeChange">
            <el-radio-button :label="undefined">全部</el-radio-button>
            <el-radio-button :label="1">文章</el-radio-button>
            <el-radio-button :label="2">动态</el-radio-button>
          </el-radio-group>
        </div>
      </div>
    </el-card>

    <!-- 资源列表 -->
    <div class="resource-list" v-loading="loading">
      <div class="resource-grid">
        <ResourceCard
          v-for="resource in resources"
          :key="resource.id"
          :resource="resource"
          @click="handleResourceClick"
        />
      </div>

      <!-- 空状态 -->
      <el-empty
        v-if="!loading && resources.length === 0"
        description="暂无学习资源"
        class="empty-state"
      />
    </div>

    <!-- 分页 -->
    <div class="pagination-container" v-if="pagination.total > 0">
      <el-pagination
        v-model:current-page="pagination.current"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        :page-sizes="[12, 24, 48]"
        layout="total, sizes, prev, pager, next, jumper"
        @current-change="handlePageChange"
        @size-change="handleSizeChange"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import { queryLearningResourcePage } from '@/api/xuexiziyuanguanli'
import ResourceCard from './components/ResourceCard.vue'

// 路由
const router = useRouter()

// 响应式数据
const loading = ref(false)
const resources = ref<API.LearningResourceVO[]>([])
const searchKeyword = ref('')
const selectedCategory = ref('')
const selectedResourceType = ref<number | undefined>(undefined)

// 分页配置
const pagination = ref({
  current: 1,
  pageSize: 12,
  total: 0
})

/**
 * 加载学习资源列表
 */
const loadResources = async () => {
  loading.value = true
  try {
    const params: API.LearningResourceQueryRequest = {
      current: pagination.value.current,
      pageSize: pagination.value.pageSize,
      sortField: 'isTop',
      sortOrder: 'desc'
    }

    // 添加筛选条件
    if (selectedCategory.value) {
      params.category = selectedCategory.value
    }
    if (selectedResourceType.value !== undefined) {
      params.resourceType = selectedResourceType.value
    }
    if (searchKeyword.value.trim()) {
      params.keyword = searchKeyword.value.trim()
    }

    const res = await queryLearningResourcePage(params)
    if (res.data.code === 200 && res.data.data) {
      resources.value = res.data.data.records || []
      pagination.value.total = Number(res.data.data.totalRow) || 0
    } else {
      ElMessage.error(res.data.message || '获取学习资源失败')
    }
  } catch (error) {
    console.error('获取学习资源失败:', error)
    ElMessage.error('获取学习资源失败')
  } finally {
    loading.value = false
  }
}

/**
 * 搜索处理
 */
const handleSearch = () => {
  pagination.value.current = 1
  loadResources()
}

/**
 * 分类变化处理
 */
const handleCategoryChange = () => {
  pagination.value.current = 1
  loadResources()
}

/**
 * 类型变化处理
 */
const handleTypeChange = () => {
  pagination.value.current = 1
  loadResources()
}

/**
 * 页码变化处理
 */
const handlePageChange = (page: number) => {
  pagination.value.current = page
  loadResources()
}

/**
 * 页面大小变化处理
 */
const handleSizeChange = (size: number) => {
  pagination.value.pageSize = size
  pagination.value.current = 1
  loadResources()
}

/**
 * 资源点击处理
 */
const handleResourceClick = (resource: API.LearningResourceVO) => {
  router.push({
    name: 'LearningResourceDetail',
    params: { id: resource.id }
  })
}

// 页面加载时获取数据
onMounted(() => {
  loadResources()
})
</script>

<style scoped>
.learning-resource-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.header-card {
  margin-bottom: 20px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 20px;
}

.title-section {
  flex: 1;
}

.page-title {
  margin: 0 0 8px 0;
  font-size: 28px;
  font-weight: 600;
  color: #303133;
}

.page-subtitle {
  margin: 0;
  color: #606266;
  font-size: 14px;
}

.search-section {
  flex: 0 0 300px;
}

.search-input {
  width: 100%;
}

.filter-card {
  margin-bottom: 20px;
}

.filter-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.filter-item {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 12px;
}

.filter-label {
  font-weight: 500;
  color: #303133;
  white-space: nowrap;
}

.resource-list {
  margin-bottom: 20px;
}

.resource-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.empty-state {
  margin: 60px 0;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 30px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .learning-resource-container {
    padding: 16px;
  }

  .header-content {
    flex-direction: column;
    align-items: stretch;
  }

  .search-section {
    flex: 1;
  }

  .filter-item {
    flex-direction: column;
    align-items: flex-start;
  }

  .resource-grid {
    grid-template-columns: 1fr;
  }
}
</style>
