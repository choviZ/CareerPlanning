<template>
  <el-card class="question-card" shadow="hover">
    <div class="question-header">
      <div class="question-number">问题 {{ currentIndex + 1 }}/{{ totalQuestions }}</div>
      <el-progress :percentage="progressPercentage" :format="progressFormat" class="question-progress" />
    </div>

    <div class="question-content">
      <h2 class="question-title">{{ question.content }}</h2>
    </div>

    <div class="options-container">
      <el-radio-group v-model="selectedOption" class="option-group">
        <el-radio
          v-for="option in question.options"
          :key="option.key"
          :label="option.key"
          class="option-item"
        >
          <div class="option-content">
            <span class="option-key">{{ option.key }}</span>
            <span class="option-value">{{ option.value }}</span>
          </div>
        </el-radio>
      </el-radio-group>
    </div>

    <div class="navigation-buttons">
      <el-button
        type="default"
        :disabled="currentIndex === 0"
        @click="handlePrevious"
      >
        上一题
      </el-button>
      <el-button
        type="primary"
        :disabled="!selectedOption"
        @click="handleNext"
      >
        {{ isLastQuestion ? '提交' : '下一题' }}
      </el-button>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { computed, ref, watch } from 'vue'

interface Props {
  question: API.AssessmentQuestionVo
  currentIndex: number
  totalQuestions: number
  userAnswer?: string
}

const props = defineProps<Props>()
const emit = defineEmits<{
  'update:userAnswer': [value: string]
  'previous': []
  'next': []
  'submit': []
}>()

const selectedOption = ref(props.userAnswer || '')

// 监听选项变化，更新父组件中的答案
watch(selectedOption, (newValue) => {
  emit('update:userAnswer', newValue)
})

// 监听props中userAnswer的变化
watch(() => props.userAnswer, (newValue) => {
  if (newValue !== undefined) {
    selectedOption.value = newValue
  }
})

// 计算进度百分比
const progressPercentage = computed(() => {
  return Math.round(((props.currentIndex + 1) / props.totalQuestions) * 100)
})

// 格式化进度条文本
const progressFormat = (percentage: number) => {
  return `${props.currentIndex + 1}/${props.totalQuestions}`
}

// 是否为最后一个问题
const isLastQuestion = computed(() => {
  return props.currentIndex === props.totalQuestions - 1
})

// 处理上一题按钮点击
const handlePrevious = () => {
  emit('previous')
}

// 处理下一题或提交按钮点击
const handleNext = () => {
  if (isLastQuestion.value) {
    emit('submit')
  } else {
    emit('next')
  }
}
</script>

<style scoped>
.question-card {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.question-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.question-number {
  font-size: 16px;
  color: #606266;
  font-weight: 500;
}

.question-progress {
  width: 200px;
}

.question-content {
  margin-bottom: 30px;
}

.question-title {
  font-size: 20px;
  color: #303133;
  line-height: 1.5;
  font-weight: 500;
}

.options-container {
  margin-bottom: 30px;
}

.option-group {
  display: flex;
  flex-direction: column;
  width: 100%;
}

.option-item {
  height: 40px;
  margin-bottom: 15px;
  padding: 12px 15px;
  border-radius: 4px;
  border: 1px solid #e4e7ed;
  transition: all 0.3s;
  width: 100%;
  display: flex;
}

.option-item:hover {
  background-color: #f5f7fa;
}



.option-content {
  display: flex;
  align-items: flex-start;
  width: 100%;
}

.option-key {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 30px;
  height: 30px;
  background-color: #f0f2f5;
  border-radius: 50%;
  margin-right: 15px;
  font-weight: bold;
  color: #606266;
  flex-shrink: 0;
}

.option-value {
  flex: 1;
  display: block;
  width: 100%;
  word-break: break-word;
  padding-top: 5px;
}

.navigation-buttons {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}
</style>
