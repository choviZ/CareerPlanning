<template>
  <div class="post-management">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>帖子管理</span>
          <el-button type="primary" @click="handleAdd">新增帖子</el-button>
        </div>
      </template>

      <!-- 搜索区域 -->
      <el-form :inline="true" :model="searchParams" class="search-form">
        <el-form-item label="帖子标题">
          <el-input v-model="searchParams.title" placeholder="请输入帖子标题"></el-input>
        </el-form-item>
        <el-form-item label="用户ID">
          <el-input v-model="searchParams.userId" placeholder="请输入用户ID"></el-input>
        </el-form-item>
        <el-form-item label="标签">
          <el-input v-model="searchParams.tags" placeholder="请输入标签"></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchParams.status" placeholder="请选择状态" clearable>
            <el-option label="正常" :value="1"></el-option>
            <el-option label="隐藏" :value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="精华">
          <el-select v-model="searchParams.isEssence" placeholder="是否精华" clearable>
            <el-option label="是" :value="1"></el-option>
            <el-option label="否" :value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格区域 -->
      <el-table :data="tableData" v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="ID" width="60"></el-table-column>
        <el-table-column prop="title" label="标题" min-width="80"
                         show-overflow-tooltip></el-table-column>
        <el-table-column prop="content" label="内容" min-width="120"
                         show-overflow-tooltip></el-table-column>
        <el-table-column prop="user.userName" label="作者" width="80"></el-table-column>
        <el-table-column label="标签" width="120">
          <template #default="{ row }">
            <el-tag v-for="tag in row.tagList" :key="tag" size="small" style="margin-right: 5px">
              {{ tag }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="viewCount" label="浏览量" width="70"></el-table-column>
        <el-table-column prop="likeCount" label="点赞数" width="70"></el-table-column>
        <el-table-column prop="commentCount" label="评论数" width="70"></el-table-column>
        <el-table-column label="状态" width="70">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'danger' : 'success'" size="small">
              {{ row.status === 1 ? '待审核' : '正常' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="精华" width="70">
          <template #default="{ row }">
            <el-switch
              v-model="row.isEssence"
              :active-value="1"
              :inactive-value="0"
              @change="handleEssenceChange(row)"
              size="small"
            />
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="150">
          <template #default="{ row }">
            {{ formatDateTime(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-space direction="vertical">
              <el-button type="primary" :icon="Edit" size="small" @click="handleEdit(row)">编辑
              </el-button>
              <el-button type="danger" :icon="Delete" size="small" @click="handleDelete(row)">删除
              </el-button>
            </el-space>
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
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="form.title" placeholder="请输入帖子标题"></el-input>
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="form.content" type="textarea" :rows="6"
                    placeholder="请输入帖子内容"></el-input>
        </el-form-item>
        <el-form-item label="标签">
          <el-input v-model="form.tagList" placeholder="请输入标签，多个标签用逗号分隔"></el-input>
        </el-form-item>
        <el-form-item label="精华">
          <el-switch
            v-model="form.isEssence"
            :active-value="1"
            :inactive-value="0"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleConfirm">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Edit, Delete } from '@element-plus/icons-vue'
import { addPost, deletePost, editPost, queryPostPage, toggleEssence } from '@/api/postController'

// 搜索参数
const searchParams = ref({
  title: '',
  userId: '',
  tags: '',
  status: undefined as number | undefined,
  isEssence: undefined as number | undefined
})

// 表格数据
const tableData = ref<API.PostVO[]>([])
const loading = ref(false)

// 分页
const pagination = ref({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 弹窗
const dialogVisible = ref(false)
const dialogTitle = ref('')
const form = ref<API.PostVO>({
  title: '',
  content: '',
  tagList: [],
  isEssence: 0
})

// 加载表格数据
const loadTableData = async () => {
  loading.value = true
  try {
    const params: API.PostQueryRequest = {
      current: pagination.value.currentPage,
      pageSize: pagination.value.pageSize,
      sortField: 'createdAt',
      sortOrder: 'desc'
    }

    // 添加搜索条件
    if (searchParams.value.title) {
      params.title = searchParams.value.title
    }
    if (searchParams.value.userId) {
      params.userId = Number(searchParams.value.userId)
    }
    if (searchParams.value.tags) {
      params.tags = searchParams.value.tags
    }
    if (searchParams.value.status !== undefined) {
      params.status = searchParams.value.status
    }
    if (searchParams.value.isEssence !== undefined) {
      params.isEssence = searchParams.value.isEssence
    }

    const res = await queryPostPage(params)
    if (res.data.code === 200 && res.data.data) {
      tableData.value = res.data.data.records || []
      pagination.value.total = Number(res.data.data.totalRow) || 0
    } else {
      ElMessage.error(res.data.message || '获取数据失败')
    }
  } catch (error) {
    console.error('获取帖子列表失败:', error)
    ElMessage.error('获取帖子列表失败')
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
  searchParams.value = {
    title: '',
    userId: '',
    tags: '',
    status: undefined,
    isEssence: undefined
  }
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

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增帖子'
  form.value = {
    title: '',
    content: '',
    tags: '',
    isEssence: 0
  }
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row: API.PostVO) => {
  dialogTitle.value = '编辑帖子'
  form.value = {
    id: row.id,
    title: row.title || '',
    content: row.content || '',
    tags: row.tagList?.join(',') || '',
    isEssence: row.isEssence || 0
  }
  dialogVisible.value = true
}

// 删除
const handleDelete = (row: API.PostVO) => {
  ElMessageBox.confirm(`确认删除帖子【${row.title}】吗？`, '提示', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const res = await deletePost({ postId: row.id! })
      if (res.data.code === 200) {
        ElMessage.success('删除成功')
        await loadTableData()
      } else {
        ElMessage.error(res.data.message || '删除失败')
      }
    } catch (error) {
      console.error('删除帖子失败:', error)
      ElMessage.error('删除失败')
    }
  })
}

// 精华状态切换
const handleEssenceChange = async (row: API.PostVO) => {
  try {
    const res = await toggleEssence({ postId: row.id! })

    if (res.data.code === 200) {
      ElMessage.success(`已${row.isEssence === 1 ? '设为' : '取消'}精华`)
    } else {
      // 如果失败，恢复原状态
      row.isEssence = row.isEssence === 1 ? 0 : 1
      ElMessage.error(res.data.message || '操作失败')
    }
  } catch (error) {
    // 如果失败，恢复原状态
    row.isEssence = row.isEssence === 1 ? 0 : 1
    console.error('精华状态切换失败:', error)
    ElMessage.error('操作失败')
  }
}

// 弹窗确认
const handleConfirm = async () => {
  try {
    let res
    if ('id' in form.value && form.value.id) {
      // 编辑
      res = await editPost(form.value as API.PostEditRequest)
    } else {
      // 新增
      res = await addPost(form.value as API.PostAddRequest)
    }

    if (res.data.code === 200) {
      ElMessage.success('id' in form.value && form.value.id ? '编辑成功' : '新增成功')
      dialogVisible.value = false
      await loadTableData()
    } else {
      ElMessage.error(res.data.message || '操作失败')
    }
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error('操作失败')
  }
}

// 格式化日期时间
const formatDateTime = (dateTime: string | undefined) => {
  if (!dateTime) return '-'
  return new Date(dateTime).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

// 页面加载时获取数据
onMounted(() => {
  loadTableData()
})
</script>

<style scoped>
.post-management {
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
</style>
