<template>
  <div class="home-container">
    <div class="hero-section">
      <h1>欢迎使用职业规划系统</h1>
      <p class="subtitle">探索您的职业潜力，规划您的未来发展</p>
      <el-button type="primary" size="large" @click="navigateToAssessment" class="cta-button">开始职业测评</el-button>
    </div>
    
    <div class="feature-section">
      <h2 class="section-title">核心功能</h2>
      <div class="feature-cards">
        <div class="card">
          <div class="card-icon">
            <el-icon><UserFilled /></el-icon>
          </div>
          <h3>职业测评</h3>
          <p>通过科学的测评模型，帮助您发现潜在职业方向</p>
          <el-button type="primary" @click="navigateToAssessment">开始测评</el-button>
        </div>
        
        <div class="card">
          <div class="card-icon">
            <el-icon><Document /></el-icon>
          </div>
          <h3>简历模板</h3>
          <p>精美的简历模板，帮助您打造专业求职形象</p>
          <el-button type="primary" @click="navigateToResumeTemplates">浏览模板</el-button>
        </div>
        
        <div class="card">
          <div class="card-icon">
            <el-icon><Reading /></el-icon>
          </div>
          <h3>学习资源</h3>
          <p>丰富的学习内容，提升您的职业技能</p>
          <el-button type="primary" @click="navigateToLearning">探索资源</el-button>
        </div>
        
        <div class="card">
          <div class="card-icon">
            <el-icon><ChatDotRound /></el-icon>
          </div>
          <h3>社区交流</h3>
          <p>与志同道合的伙伴分享经验，交流心得</p>
          <el-button type="primary" @click="navigateToCommunity">加入社区</el-button>
        </div>
        

      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { UserFilled, Document, Reading, ChatDotRound } from '@element-plus/icons-vue'

const router = useRouter()

const navigateToAssessment = () => {
  router.push('/assessment/entry')
}

const navigateToResumeTemplates = () => {
  router.push('/resume/templates')
}

const navigateToLearning = () => {
  router.push('/learning')
}

const navigateToCommunity = () => {
  router.push('/community')
}
</script>

<style scoped>
.home-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  text-align: center;
}

/* 英雄区域样式 */
.hero-section {
  padding: 80px 20px;
  margin-bottom: 60px;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e8f0 100%);
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
}

.hero-section h1 {
  font-size: 3rem;
  margin-bottom: 20px;
  color: #333;
  font-weight: 700;
}

.subtitle {
  font-size: 1.4rem;
  color: #666;
  margin-bottom: 40px;
  max-width: 700px;
  margin-left: auto;
  margin-right: auto;
}

.cta-button {
  padding: 12px 30px;
  font-size: 1.2rem;
  font-weight: 500;
}

/* 功能区域样式 */
.feature-section {
  padding: 40px 0 80px;
}

.section-title {
  font-size: 2.2rem;
  margin-bottom: 50px;
  color: #333;
  position: relative;
  display: inline-block;
}

.section-title::after {
  content: '';
  position: absolute;
  bottom: -10px;
  left: 50%;
  transform: translateX(-50%);
  width: 60px;
  height: 4px;
  background-color: #4096ff;
  border-radius: 2px;
}

.feature-cards {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 30px;
}

.card {
  width: 280px;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
  background-color: #fff;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.card:hover {
  transform: translateY(-10px);
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.12);
}

.card-icon {
  width: 70px;
  height: 70px;
  border-radius: 50%;
  background-color: #f0f7ff;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
}

.card-icon .el-icon {
  font-size: 30px;
  color: #4096ff;
}

.card h3 {
  font-size: 1.5rem;
  margin-bottom: 15px;
  color: #333;
  font-weight: 600;
}

.card p {
  margin-bottom: 25px;
  color: #666;
  line-height: 1.6;
  flex-grow: 1;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .feature-cards {
    padding: 0 20px;
  }
}

@media (max-width: 768px) {
  .hero-section {
    padding: 60px 20px;
  }
  
  .hero-section h1 {
    font-size: 2.5rem;
  }
  
  .subtitle {
    font-size: 1.2rem;
  }
  
  .card {
    width: calc(50% - 20px);
    min-width: 250px;
  }
}

@media (max-width: 576px) {
  .hero-section h1 {
    font-size: 2rem;
  }
  
  .subtitle {
    font-size: 1.1rem;
  }
  
  .card {
    width: 100%;
  }
}
</style>