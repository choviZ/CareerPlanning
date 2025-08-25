<template>
  <div class="assessment-container">
    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="10" animated />
    </div>

    <div v-else-if="showResult" class="result-container">
      <ResultCard :result="assessmentResult" :test-type="testType" />
    </div>
    <div v-else-if="questions.length > 0" class="questions-container">
      <QuestionCard
        :question="questions[currentQuestionIndex]"
        :current-index="currentQuestionIndex"
        :total-questions="questions.length"
        :user-answer="userAnswers[currentQuestionIndex]"
        @update:user-answer="updateAnswer"
        @previous="previousQuestion"
        @next="nextQuestion"
        @submit="submitAssessment"
      />
    </div>

    <div v-else class="error-container">
      {{ questions }}
      <el-empty description="暂无测评题目" />
      <el-button type="primary" @click="goBack">返回</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { queryQuestion, doAssessment } from '@/api/assessmentQuestionController'
import QuestionCard from '@/components/assessment/QuestionCard.vue'
import ResultCard from '@/components/assessment/ResultCard.vue'

const router = useRouter()
const loading = ref(true)
const questions = ref<API.AssessmentQuestionVo[]>([])
const testType = 'MBTI'
const currentQuestionIndex = ref(0)
const userAnswers = ref<string[]>([])
const showResult = ref(false)
const assessmentResult = ref<API.UserAssessmentVo>({})

// 获取问题列表
const fetchQuestions = async () => {
  loading.value = true
  try {
    const res = await queryQuestion({ testType })
    if (res.data.code === 200 && res.data.data) {
      questions.value = res.data.data
      // 初始化用户答案数组
      userAnswers.value = new Array(res.data.data.length).fill('')
    } else {
      ElMessage.error(res.data.message || '获取题目失败')
    }
  } catch (error) {
    console.error('获取题目出错:', error)
    ElMessage.error('获取题目出错')
  } finally {
    loading.value = false
  }
}

// 更新用户答案
const updateAnswer = (answer: string) => {
  userAnswers.value[currentQuestionIndex.value] = answer
}

// 上一题
const previousQuestion = () => {
  if (currentQuestionIndex.value > 0) {
    currentQuestionIndex.value--
  }
}

// 下一题
const nextQuestion = () => {
  if (currentQuestionIndex.value < questions.value.length - 1) {
    currentQuestionIndex.value++
  }
}

// 提交测评
const submitAssessment = async () => {
  // 检查是否所有题目都已回答
  const allAnswered = userAnswers.value.every(answer => answer !== '')
  if (!allAnswered) {
    ElMessage.warning('请回答所有问题后再提交')
    return
  }

  loading.value = true
  try {
    const res = await doAssessment({
      testType,
      userAnswers: userAnswers.value
    })
    if (res.data.code === 200 && res.data.data) {
      assessmentResult.value = res.data.data
      showResult.value = true
    } else {
      ElMessage.error(res.data.message || '提交测评失败')
    }
  } catch (error) {
    console.error('提交测评出错:', error)
    ElMessage.error('提交测评出错')
  } finally {
    loading.value = false
  }
}

// 返回上一页
const goBack = () => {
  router.back()
}

// 页面加载前获取数据
onMounted(() => {
  fetchQuestions()
})
</script>

<style scoped>
.assessment-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}

.loading-container,
.questions-container,
.result-container,
.error-container {
  margin: 20px auto;
}

.error-container {
  text-align: center;
  padding: 40px 0;
}
</style>
