<template>
  <div class="home-container">
    <h1>欢迎使用职业规划系统</h1>
    <p>探索您的职业潜力，规划您的未来发展</p>
    <div class="feature-cards">
      <div class="card">
        <h3>职业测评</h3>
        <p>通过科学的测评模型，帮助您发现潜在职业方向</p>
        <el-button type="primary" @click="navigateToAssessment">开始测评</el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

const navigateToAssessment = () => {
  router.push('/assessment/entry')
}
</script>

<style scoped>
.home-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
  text-align: center;
}

h1 {
  font-size: 2.5rem;
  margin-bottom: 20px;
  color: #333;
}

p {
  font-size: 1.2rem;
  color: #666;
  margin-bottom: 40px;
}

.feature-cards {
  display: flex;
  justify-content: center;
  gap: 30px;
}

.card {
  width: 300px;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  background-color: #fff;
}

.card h3 {
  font-size: 1.5rem;
  margin-bottom: 15px;
  color: #4096ff;
}

.card p {
  margin-bottom: 20px;
}
</style>