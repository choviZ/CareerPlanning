<template>
  <el-card class="result-card" shadow="hover">
    <div class="result-header">
      <h2 class="result-title">测评结果</h2>
      <div class="result-type">{{ testType }} 测评</div>
    </div>

    <div class="result-content">
      <div class="result-code">
        <h3>您的类型是：</h3>
        <div class="code-badge">{{ result.resultCode }}</div>
      </div>

      <div class="dimension-scores" v-if="dimensionScores.length > 0">
        <h3>维度得分：</h3>
        <div class="scores-container">
          <div 
            v-for="(score, index) in dimensionScores" 
            :key="index"
            class="score-item"
          >
            <div class="dimension-name">{{ score.dimension }}</div>
            <el-progress 
              :percentage="score.score" 
              :color="getProgressColor(score.score)"
              :format="progressFormat"
              :stroke-width="15"
            />
          </div>
        </div>
      </div>
    </div>

    <div class="result-actions">
      <el-button type="primary" @click="handleRetry">重新测评</el-button>
      <el-button type="default" @click="handleBack">返回首页</el-button>
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'

interface Props {
  result: API.AssessmentResultVo
  testType: string
}

const props = defineProps<Props>()
const router = useRouter()

// 解析维度得分
const dimensionScores = computed(() => {
  if (!props.result.dimensionScores) return []
  
  try {
    return JSON.parse(props.result.dimensionScores)
  } catch (e) {
    console.error('解析维度得分失败', e)
    return []
  }
})

// 格式化进度条文本
const progressFormat = (percentage: number) => {
  return `${percentage}%`
}

// 根据得分获取进度条颜色
const getProgressColor = (score: number) => {
  if (score < 30) return '#909399' // 灰色
  if (score < 50) return '#e6a23c' // 黄色
  if (score < 70) return '#409eff' // 蓝色
  return '#67c23a' // 绿色
}

// 重新测评
const handleRetry = () => {
  router.push('/assessment')
}

// 返回首页
const handleBack = () => {
  router.push('/')
}
</script>

<style scoped>
.result-card {
  max-width: 800px;
  margin: 0 auto;
  padding: 30px;
}

.result-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  border-bottom: 1px solid #ebeef5;
  padding-bottom: 20px;
}

.result-title {
  font-size: 24px;
  color: #303133;
  font-weight: 600;
  margin: 0;
}

.result-type {
  font-size: 16px;
  color: #606266;
  background-color: #f0f2f5;
  padding: 5px 12px;
  border-radius: 4px;
}

.result-content {
  margin-bottom: 30px;
}

.result-code {
  margin-bottom: 30px;
  text-align: center;
}

.result-code h3 {
  font-size: 18px;
  color: #606266;
  margin-bottom: 15px;
}

.code-badge {
  display: inline-block;
  font-size: 28px;
  font-weight: 600;
  color: #fff;
  background-color: #409eff;
  padding: 10px 25px;
  border-radius: 6px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.dimension-scores h3 {
  font-size: 18px;
  color: #606266;
  margin-bottom: 20px;
}

.scores-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.score-item {
  display: flex;
  align-items: center;
}

.dimension-name {
  width: 120px;
  font-size: 16px;
  color: #303133;
  margin-right: 15px;
}

.result-actions {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 30px;
}
</style>