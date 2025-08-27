<template>
  <el-dialog
    v-model="visible"
    :title="title"
    width="60%"
    :before-close="handleClose"
    class="resume-preview-dialog"
    @closed="handleClosed"
    draggable
  >
    <div class="preview-container">
      <ResumePreview
        v-if="resumeData"
        :resume-data="resumeData"
        :template-config="templateConfig"
      />
      <div v-else class="loading-placeholder">
        <el-icon class="is-loading">
          <Loading />
        </el-icon>
        <p>加载中...</p>
      </div>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">{{ cancelText }}</el-button>
        <el-button
          v-if="showConfirmButton"
          type="primary"
          @click="handleConfirm"
        >
          {{ confirmText }}
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { Loading } from '@element-plus/icons-vue'
import ResumePreview from './ResumePreview.vue'

interface Props {
  modelValue: boolean
  title?: string
  resumeData?: {
    content: any
  } | null
  templateConfig?: any
  showConfirmButton?: boolean
  confirmText?: string
  cancelText?: string
}

interface Emits {
  (e: 'update:modelValue', value: boolean): void
  (e: 'confirm'): void
  (e: 'cancel'): void
  (e: 'closed'): void
}

const props = withDefaults(defineProps<Props>(), {
  title: '简历预览',
  showConfirmButton: true,
  confirmText: '确认',
  cancelText: '取消'
})

const emit = defineEmits<Emits>()

// 控制弹窗显示
const visible = computed({
  get: () => props.modelValue,
  set: (value: boolean) => emit('update:modelValue', value)
})

// 处理关闭
const handleClose = () => {
  emit('cancel')
  visible.value = false
}

// 处理确认
const handleConfirm = () => {
  emit('confirm')
}

// 处理弹窗关闭后的回调
const handleClosed = () => {
  emit('closed')
}
</script>

<style scoped>
.resume-preview-dialog {
  .preview-container {
    height: 70vh;
    overflow-y: auto;
    border: 1px solid #e4e7ed;
    border-radius: 8px;
    padding: 0;
    background-color: #f5f7fa;
    display: flex;
    justify-content: center;
    align-items: flex-start;
    padding-top: 20px;
  }

  .preview-container :deep(.resume-preview) {
    transform: scale(0.8);
    transform-origin: top center;
    margin: 0;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  }

  .loading-placeholder {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 200px;
    color: #909399;

    .el-icon {
      font-size: 32px;
      margin-bottom: 16px;
    }

    p {
      margin: 0;
      font-size: 14px;
    }
  }
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>
