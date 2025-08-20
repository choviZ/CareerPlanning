<template>
  <div class="assessment-container">
    <el-card class="assessment-card">
      <div class="content-wrapper">
        <!-- 文字部分 -->
        <div class="text-section">
          <h1 class="title">职业倾向测评系统</h1>
          <p class="description">通过科学的测评模型，帮助您发现潜在职业方向</p>
          <div class="action-section">
            <el-select v-model="testType" class="type-select">
              <el-option label="MBTI职业性格测试" value="MBTI" />
            </el-select>
            <el-button
              type="primary"
              class="start-btn"
              @click="startAssessment"
            >
              开始测评
            </el-button>
          </div>
        </div>
        <!-- 插图 -->
        <div class="illustration-section">
          <img src="@/assets/assessment.svg" class="illustration" alt="image" />
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const testType = ref('MBTI')
const router = useRouter()

const startAssessment = () => {
  router.push({
    path: '/assessmentPage'
  })
}
</script>

<style scoped>
.assessment-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 180px);
}

.assessment-card {
  width: 100%;
  padding: 40px;
  max-width: 1200px;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.content-wrapper {
  display: flex;
  gap: 40px;
  align-items: center;
}

.text-section {
  flex: 1;
  padding: 20px 0;
}

.title {
  font-size: 2rem;
  font-weight: 600;
  color: #333;
  margin-bottom: 16px;
}

.description {
  font-size: 1.1rem;
  color: #666;
  margin-bottom: 30px;
  line-height: 1.6;
}

.action-section {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.type-select {
  width: 100%;
  max-width: 300px;
}

.start-btn {
  width: 100%;
  max-width: 300px;
  height: 42px;
  font-size: 1rem;
}

.illustration-section {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
}

.illustration {
  max-width: 100%;
  height: auto;
}

@media (max-width: 768px) {
  .content-wrapper {
    flex-direction: column;
  }

  .text-section, .illustration-section {
    width: 100%;
  }

  .assessment-card {
    margin: 10px;
  }
}
</style>
