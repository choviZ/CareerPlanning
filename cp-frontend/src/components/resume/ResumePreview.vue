<template>
  <div class="resume-preview" :style="resumeStyles">
    <!-- 基本信息 -->
    <div class="basic-info" v-if="resumeData.content.basicInfo">
      <h1 class="name" :style="nameStyles">{{ resumeData.content.basicInfo.name || '姓名' }}</h1>
      <div class="contact-info" :style="contactStyles">
        <span v-if="resumeData.content.basicInfo.email" class="contact-item">
          <i class="contact-icon">📧</i>
          {{ resumeData.content.basicInfo.email }}
        </span>
        <span v-if="resumeData.content.basicInfo.phone" class="contact-item">
          <i class="contact-icon">📱</i>
          {{ resumeData.content.basicInfo.phone }}
        </span>
        <span v-if="resumeData.content.basicInfo.address" class="contact-item">
          <i class="contact-icon">📍</i>
          {{ resumeData.content.basicInfo.address }}
        </span>
      </div>
      <div v-if="resumeData.content.basicInfo.jobIntention" class="job-intention" :style="jobIntentionStyles">
        <strong>求职意向：</strong>{{ resumeData.content.basicInfo.jobIntention }}
      </div>
    </div>

    <!-- 布局容器 -->
    <div class="resume-content" :class="layoutClass">
      <!-- 左栏（双栏布局时使用） -->
      <div v-if="isDoubleColumn" class="left-column">
        <!-- 教育经历 -->
        <ResumeSection 
          v-if="resumeData.content.education && resumeData.content.education.length > 0"
          title="教育经历" 
          :config="sectionConfigs.education"
          :style="sectionStyles"
        >
          <div v-for="(edu, index) in resumeData.content.education" :key="index" class="education-item">
            <div class="education-header">
              <h4 class="school-name">{{ edu.schoolName }}</h4>
              <span class="education-time">{{ formatDateRange(edu.startDate, edu.endDate) }}</span>
            </div>
            <div class="education-details">
              <p class="major">{{ edu.major }} - {{ edu.degree }}</p>
              <p v-if="edu.gpa" class="gpa">GPA: {{ edu.gpa }}</p>
            </div>
          </div>
        </ResumeSection>

        <!-- 技能 -->
        <ResumeSection 
          v-if="resumeData.content.skills && resumeData.content.skills.length > 0"
          title="专业技能" 
          :config="sectionConfigs.skills"
          :style="sectionStyles"
        >
          <div class="skills-container">
            <div v-for="(skill, index) in resumeData.content.skills" :key="index" class="skill-item">
              <div class="skill-header">
                <span class="skill-name">{{ skill.skillName }}</span>
                <span v-if="skill.skillLevel" class="skill-level">{{ skill.skillLevel }}%</span>
              </div>
              <div v-if="skill.skillLevel && sectionConfigs.skills?.displayType === 'progress'" 
                   class="skill-progress">
                <div class="progress-bar" :style="{
                  width: skill.skillLevel + '%',
                  backgroundColor: sectionConfigs.skills?.progressColor || '#3498db',
                  height: sectionConfigs.skills?.progressHeight || '8px'
                }"></div>
              </div>
            </div>
          </div>
        </ResumeSection>
      </div>

      <!-- 右栏（双栏布局时使用）或主栏（单栏布局时使用） -->
      <div :class="isDoubleColumn ? 'right-column' : 'main-column'">
        <!-- 工作经历 -->
        <ResumeSection 
          v-if="resumeData.content.workExperience && resumeData.content.workExperience.length > 0"
          title="工作经历" 
          :config="sectionConfigs.workExperience"
          :style="sectionStyles"
        >
          <div v-for="(work, index) in resumeData.content.workExperience" :key="index" class="work-item">
            <div class="work-header">
              <h4 class="company-name">{{ work.companyName }}</h4>
              <span class="work-time">{{ formatDateRange(work.startDate, work.endDate) }}</span>
            </div>
            <div class="work-details">
              <p class="position">{{ work.position }}</p>
              <div v-if="work.description" class="work-description">
                <p v-for="(desc, i) in work.description.split('\n')" :key="i">{{ desc }}</p>
              </div>
            </div>
          </div>
        </ResumeSection>

        <!-- 项目经历 -->
        <ResumeSection 
          v-if="resumeData.content.projectExperience && resumeData.content.projectExperience.length > 0"
          title="项目经历" 
          :config="sectionConfigs.projectExperience"
          :style="sectionStyles"
        >
          <div v-for="(project, index) in resumeData.content.projectExperience" :key="index" class="project-item">
            <div class="project-header">
              <h4 class="project-name">{{ project.projectName }}</h4>
              <span class="project-time">{{ formatDateRange(project.startDate, project.endDate) }}</span>
            </div>
            <div class="project-details">
              <p v-if="project.role" class="project-role">角色：{{ project.role }}</p>
              <p v-if="project.technologies" class="project-tech">技术栈：{{ project.technologies }}</p>
              <div v-if="project.description" class="project-description">
                <p v-for="(desc, i) in project.description.split('\n')" :key="i">{{ desc }}</p>
              </div>
            </div>
          </div>
        </ResumeSection>

        <!-- 单栏布局时的教育经历和技能 -->
        <template v-if="!isDoubleColumn">
          <!-- 教育经历 -->
          <ResumeSection 
            v-if="resumeData.content.education && resumeData.content.education.length > 0"
            title="教育经历" 
            :config="sectionConfigs.education"
            :style="sectionStyles"
          >
            <div v-for="(edu, index) in resumeData.content.education" :key="index" class="education-item">
              <div class="education-header">
                <h4 class="school-name">{{ edu.schoolName }}</h4>
                <span class="education-time">{{ formatDateRange(edu.startDate, edu.endDate) }}</span>
              </div>
              <div class="education-details">
                <p class="major">{{ edu.major }} - {{ edu.degree }}</p>
                <p v-if="edu.gpa" class="gpa">GPA: {{ edu.gpa }}</p>
              </div>
            </div>
          </ResumeSection>

          <!-- 技能 -->
          <ResumeSection 
            v-if="resumeData.content.skills && resumeData.content.skills.length > 0"
            title="专业技能" 
            :config="sectionConfigs.skills"
            :style="sectionStyles"
          >
            <div class="skills-container">
              <div v-for="(skill, index) in resumeData.content.skills" :key="index" class="skill-item">
                <div class="skill-header">
                  <span class="skill-name">{{ skill.skillName }}</span>
                  <span v-if="skill.skillLevel" class="skill-level">{{ skill.skillLevel }}%</span>
                </div>
                <div v-if="skill.skillLevel && sectionConfigs.skills?.displayType === 'progress'" 
                     class="skill-progress">
                  <div class="progress-bar" :style="{
                    width: skill.skillLevel + '%',
                    backgroundColor: sectionConfigs.skills?.progressColor || '#3498db',
                    height: sectionConfigs.skills?.progressHeight || '8px'
                  }"></div>
                </div>
              </div>
            </div>
          </ResumeSection>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import ResumeSection from './ResumeSection.vue'

interface Props {
  resumeData: {
    content: any
    templateConfig?: any
  }
  templateConfig?: any
}

const props = defineProps<Props>()

// 获取模板配置
const config = computed(() => props.templateConfig || props.resumeData.templateConfig || {})

// 布局类型
const isDoubleColumn = computed(() => {
  return config.value.layout?.type === 'two-column'
})

const layoutClass = computed(() => {
  return isDoubleColumn.value ? 'two-column-layout' : 'single-column-layout'
})

// 样式计算
const resumeStyles = computed(() => {
  const colors = config.value.colors || {}
  const typography = config.value.typography || {}
  
  return {
    fontFamily: typography.fontFamily || '\'Microsoft YaHei\', \'PingFang SC\', sans-serif',
    fontSize: typography.fontSize || '14px',
    lineHeight: typography.lineHeight || '1.6',
    color: colors.text || '#2c3e50',
    backgroundColor: colors.background || '#ffffff'
  }
})

const nameStyles = computed(() => {
  const colors = config.value.colors || {}
  return {
    color: colors.primary || '#2c3e50',
    fontSize: '28px',
    fontWeight: 'bold',
    marginBottom: '10px'
  }
})

const contactStyles = computed(() => {
  const colors = config.value.colors || {}
  return {
    color: colors.secondary || '#3498db',
    fontSize: '14px'
  }
})

const jobIntentionStyles = computed(() => {
  const colors = config.value.colors || {}
  return {
    color: colors.text || '#2c3e50',
    fontSize: '16px',
    marginTop: '15px',
    padding: '10px',
    backgroundColor: colors.background || '#f8f9fa',
    borderLeft: `4px solid ${colors.primary || '#2c3e50'}`
  }
})

const sectionStyles = computed(() => {
  const colors = config.value.colors || {}
  return {
    marginBottom: '25px',
    borderBottom: `1px solid ${colors.secondary || '#e9ecef'}`
  }
})

// 区域配置
const sectionConfigs = computed(() => {
  return config.value.sections || {}
})

// 日期格式化
const formatDateRange = (startDate: string, endDate: string) => {
  if (!startDate) return ''
  const start = new Date(startDate).toLocaleDateString('zh-CN', { year: 'numeric', month: '2-digit' })
  const end = endDate ? new Date(endDate).toLocaleDateString('zh-CN', { year: 'numeric', month: '2-digit' }) : '至今'
  return `${start} - ${end}`
}
</script>

<style scoped>
.resume-preview {
  max-width: 800px;
  margin: 0 auto;
  padding: 40px;
  background: white;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
  min-height: 1000px;
}

.basic-info {
  text-align: center;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 2px solid #e9ecef;
}

.name {
  margin: 0 0 10px 0;
}

.contact-info {
  display: flex;
  justify-content: center;
  gap: 20px;
  flex-wrap: wrap;
  margin-bottom: 15px;
}

.contact-item {
  display: flex;
  align-items: center;
  gap: 5px;
}

.contact-icon {
  font-size: 16px;
}

.job-intention {
  text-align: left;
  border-radius: 4px;
}

.resume-content {
  margin-top: 30px;
}

.two-column-layout {
  display: grid;
  grid-template-columns: 1fr 2fr;
  gap: 30px;
}

.single-column-layout {
  display: block;
}

.left-column,
.right-column,
.main-column {
  min-height: 200px;
}

.education-item,
.work-item,
.project-item {
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #f0f0f0;
}

.education-item:last-child,
.work-item:last-child,
.project-item:last-child {
  border-bottom: none;
}

.education-header,
.work-header,
.project-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 8px;
}

.school-name,
.company-name,
.project-name {
  margin: 0;
  font-size: 16px;
  font-weight: bold;
  color: #2c3e50;
}

.education-time,
.work-time,
.project-time {
  font-size: 12px;
  color: #666;
  white-space: nowrap;
}

.education-details,
.work-details,
.project-details {
  margin-left: 0;
}

.major,
.position,
.project-role,
.project-tech {
  margin: 5px 0;
  font-weight: 500;
}

.gpa {
  margin: 5px 0;
  color: #666;
  font-size: 13px;
}

.work-description,
.project-description {
  margin-top: 8px;
  line-height: 1.6;
}

.work-description p,
.project-description p {
  margin: 3px 0;
  color: #555;
}

.skills-container {
  display: grid;
  gap: 12px;
}

.skill-item {
  margin-bottom: 10px;
}

.skill-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 5px;
}

.skill-name {
  font-weight: 500;
  color: #2c3e50;
}

.skill-level {
  font-size: 12px;
  color: #666;
}

.skill-progress {
  width: 100%;
  background-color: #e9ecef;
  border-radius: 4px;
  overflow: hidden;
  height: 8px;
}

.progress-bar {
  height: 100%;
  border-radius: 4px;
  transition: width 0.3s ease;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .resume-preview {
    padding: 20px;
  }
  
  .two-column-layout {
    grid-template-columns: 1fr;
    gap: 20px;
  }
  
  .contact-info {
    flex-direction: column;
    gap: 10px;
  }
  
  .education-header,
  .work-header,
  .project-header {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .education-time,
  .work-time,
  .project-time {
    margin-top: 5px;
  }
}
</style>