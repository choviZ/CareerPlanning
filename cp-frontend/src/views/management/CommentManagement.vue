<template>
  <div class="comment-management">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>评论管理</span>
        </div>
      </template>

      <!-- 搜索区域 -->
      <div class="search-area">
        <el-form :model="searchParams" inline>
          <el-form-item label="帖子ID">
            <el-input
              v-model="searchParams.postId"
              placeholder="请输入帖子ID"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="用户ID">
            <el-input
              v-model="searchParams.userId"
              placeholder="请输入用户ID"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="评论内容">
            <el-input
              v-model="searchParams.content"
              placeholder="请输入评论内容关键词"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="评论层级">
            <el-select
              v-model="searchParams.level"
              placeholder="请选择评论层级"
              clearable
              style="width: 150px"
            >
              <el-option label="一级评论" :value="1" />
              <el-option label="二级评论" :value="2" />
              <el-option label="三级评论" :value="3" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 表格区域 -->
      <div class="table-area">
        <el-table
          :data="tableData"
          style="width: 100%"
          v-loading="loading"
          border
        >
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="postId" label="帖子ID" width="100" />
          <el-table-column label="用户信息" width="150">
            <template #default="scope">
              <div v-if="scope.row.user">
                <div>{{ scope.row.user.username }}</div>
                <div class="text-gray-500 text-sm">ID: {{ scope.row.userId }}</div>
              </div>
              <div v-else>用户ID: {{ scope.row.userId }}</div>
            </template>
          </el-table-column>
          <el-table-column label="评论内容" min-width="200">
            <template #default="scope">
              <div class="comment-content">
                {{ scope.row.content }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="回复信息" width="150">
            <template #default="scope">
              <div v-if="scope.row.replyToUser">
                <div>回复: {{ scope.row.replyToUser.username }}</div>
                <div class="text-gray-500 text-sm">ID: {{ scope.row.replyToUserId }}</div>
              </div>
              <div v-else-if="scope.row.parentId">
                <div class="text-gray-500 text-sm">父评论ID: {{ scope.row.parentId }}</div>
              </div>
              <div v-else>-</div>
            </template>
          </el-table-column>
          <el-table-column prop="level" label="层级" width="80">
            <template #default="scope">
              <el-tag :type="getLevelTagType(scope.row.level)">{{ scope.row.level }}级</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="likeCount" label="点赞数" width="100" />
          <el-table-column prop="replyCount" label="回复数" width="100" />
          <el-table-column prop="createdAt" label="创建时间" width="180">
            <template #default="scope">
              {{ formatDate(scope.row.createdAt) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120" fixed="right">
            <template #default="scope">
              <el-button
                type="danger"
                size="small"
                @click="handleDelete(scope.row)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

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
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listCommentByPage, deleteComment } from '@/api/commentController'

// 响应式数据
const loading = ref(false)
const tableData = ref<API.CommentVO[]>([])

// 搜索参数
const searchParams = reactive({
  postId: '',
  userId: '',
  content: '',
  level: undefined as number | undefined
})

// 分页信息
const pagination = ref({
  currentPage: 1,
  pageSize: 20,
  total: 0
})

// 加载表格数据
const loadTableData = async () => {
  loading.value = true
  try {
    const params: API.CommentQueryRequest = {
      current: pagination.value.currentPage,
      pageSize: pagination.value.pageSize,
      sortField: 'createdAt',
      sortOrder: 'desc'
    }

    // 添加搜索条件
    if (searchParams.postId) {
      params.postId = Number(searchParams.postId)
    }
    if (searchParams.userId) {
      params.userId = Number(searchParams.userId)
    }
    if (searchParams.content) {
      params.content = searchParams.content
    }
    if (searchParams.level !== undefined) {
      params.level = searchParams.level
    }

    const res = await listCommentByPage(params)
    if (res.data.code === 200 && res.data.data) {
      tableData.value = res.data.data.records || []
      pagination.value.total = Number(res.data.data.totalRow) || 0
    } else {
      ElMessage.error(res.data.message || '获取评论列表失败')
    }
  } catch (error) {
    console.error('获取评论列表失败:', error)
    ElMessage.error('获取评论列表失败')
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
  searchParams.postId = ''
  searchParams.userId = ''
  searchParams.content = ''
  searchParams.level = undefined
  pagination.value.currentPage = 1
  loadTableData()
}

// 分页变化
const handleCurrentChange = (page: number) => {
  pagination.value.currentPage = page
  loadTableData()
}

// 页面大小变化
const handleSizeChange = (size: number) => {
  pagination.value.pageSize = size
  pagination.value.currentPage = 1
  loadTableData()
}

// 删除评论
const handleDelete = async (row: API.CommentVO) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除这条评论吗？\n评论内容：${row.content}`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const response = await deleteComment({ id: row.id })
    if (response.data.code === 200) {
      ElMessage.success('删除成功')
      await loadTableData()
    } else {
      ElMessage.error(response.data.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除评论失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 获取层级标签类型
const getLevelTagType = (level: number) => {
  switch (level) {
    case 1:
      return 'primary'
    case 2:
      return 'success'
    case 3:
      return 'warning'
    default:
      return 'info'
  }
}

// 格式化日期
const formatDate = (dateString: string) => {
  if (!dateString) return '-'
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

// 组件挂载时加载数据
onMounted(() => {
  loadTableData()
})
</script>

<style scoped>
.comment-management {
  padding: 20px;
}

.box-card {
  border-radius: 8px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
  font-size: 16px;
}

.search-area {
  margin-bottom: 20px;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 6px;
}

.table-area {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.comment-content {
  max-width: 200px;
  word-break: break-word;
  line-height: 1.4;
}

.text-gray-500 {
  color: #6b7280;
}

.text-sm {
  font-size: 12px;
}
</style>
