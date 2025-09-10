<template>
  <div class="resume-editor">
    <!-- 顶部工具栏 -->
    <div class="editor-header">
      <div class="header-left">
        <h2>创建简历</h2>
        <div class="header-form">
          <el-form :model="resumeSettings" layout="inline" size="small">
            <el-form-item label="简历标题">
              <el-input v-model="resumeSettings.title" placeholder="请输入简历标题" style="width: 200px;" />
            </el-form-item>
            <el-form-item label="状态">
              <el-select v-model="resumeSettings.status" placeholder="请选择状态" style="width: 120px;">
                <el-option label="草稿" :value="1" />
                <el-option label="已完成" :value="2" />
                <el-option label="已发布" :value="3" />
              </el-select>
            </el-form-item>
            <el-form-item label="是否公开">
              <el-select v-model="resumeSettings.isPublic" placeholder="请选择" style="width: 100px;">
                <el-option label="私有" :value="0" />
                <el-option label="公开" :value="1" />
              </el-select>
            </el-form-item>
          </el-form>
        </div>
      </div>
      <div class="header-right">
        <el-button type="primary" @click="publishResume" :loading="publishing">
          发布简历
        </el-button>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="editor-content">
      <!-- 左侧编辑区域 -->
      <div class="editor-left">
        <div class="section-list">
          <!-- 基本信息 -->
          <div class="resume-section" v-if="resumeData.basicInfo">
            <div class="section-header">
              <h3>基本信息</h3>
            </div>
            <div class="section-content">
              <el-form :model="resumeData.basicInfo" label-width="80px">
                <el-form-item label="姓名">
                  <el-input v-model="resumeData.basicInfo.name" placeholder="请输入姓名" />
                </el-form-item>
                <el-form-item label="邮箱">
                  <el-input v-model="resumeData.basicInfo.email" placeholder="请输入邮箱" />
                </el-form-item>
                <el-form-item label="电话">
                  <el-input v-model="resumeData.basicInfo.phone" placeholder="请输入电话" />
                </el-form-item>
                <el-form-item label="地址">
                  <el-input v-model="resumeData.basicInfo.address" placeholder="请输入地址" />
                </el-form-item>
              </el-form>
            </div>
          </div>

          <!-- 教育经历 -->
          <div class="resume-section" v-if="resumeData.education">
            <div class="section-header">
              <h3>教育经历</h3>
              <el-button type="text" @click="addEducation">+ 添加</el-button>
            </div>
            <div class="section-content">
              <div v-for="(edu, index) in resumeData.education" :key="index" class="education-item">
                <el-form :model="edu" label-width="80px">
                  <el-form-item label="学校">
                    <el-input v-model="edu.schoolName" placeholder="请输入学校名称" />
                  </el-form-item>
                  <el-form-item label="专业">
                    <el-input v-model="edu.major" placeholder="请输入专业" />
                  </el-form-item>
                  <el-form-item label="学历">
                    <el-select v-model="edu.degree" placeholder="请选择学历">
                      <el-option label="高中" value="高中" />
                      <el-option label="大专" value="大专" />
                      <el-option label="本科" value="本科" />
                      <el-option label="硕士" value="硕士" />
                      <el-option label="博士" value="博士" />
                    </el-select>
                  </el-form-item>
                  <el-form-item label="时间">
                    <el-date-picker
                      v-model="edu.startDate"
                      type="date"
                      placeholder="开始时间"
                      style="width: 45%; margin-right: 10px;"
                    />
                    <el-date-picker
                      v-model="edu.endDate"
                      type="date"
                      placeholder="结束时间"
                      style="width: 45%;"
                    />
                  </el-form-item>
                  <el-form-item>
                    <el-button type="danger" size="small" @click="removeEducation(index)">删除
                    </el-button>
                  </el-form-item>
                </el-form>
              </div>
            </div>
          </div>

          <!-- 工作经验 -->
          <div class="resume-section" v-if="resumeData.workExperience">
            <div class="section-header">
              <h3>工作经验</h3>
              <el-button type="text" @click="addWorkExperience">+ 添加</el-button>
            </div>
            <div class="section-content">
              <div v-for="(work, index) in resumeData.workExperience" :key="index"
                   class="work-item">
                <el-form :model="work" label-width="80px">
                  <el-form-item label="公司">
                    <el-input v-model="work.companyName" placeholder="请输入公司名称" />
                  </el-form-item>
                  <el-form-item label="职位">
                    <el-input v-model="work.position" placeholder="请输入职位" />
                  </el-form-item>
                  <el-form-item label="时间">
                    <el-date-picker
                      v-model="work.startDate"
                      type="date"
                      placeholder="开始时间"
                      style="width: 45%; margin-right: 10px;"
                    />
                    <el-date-picker
                      v-model="work.endDate"
                      type="date"
                      placeholder="结束时间"
                      style="width: 45%;"
                    />
                  </el-form-item>
                  <el-form-item label="描述">
                    <el-input
                      v-model="work.jobDescription"
                      type="textarea"
                      :rows="3"
                      placeholder="请输入工作描述"
                    />
                  </el-form-item>
                  <el-form-item>
                    <el-button type="danger" size="small" @click="removeWorkExperience(index)">
                      删除
                    </el-button>
                  </el-form-item>
                </el-form>
              </div>
            </div>
          </div>

          <!-- 项目经历 -->
          <div class="resume-section" v-if="resumeData.projectExperience">
            <div class="section-header">
              <h3>项目经历</h3>
              <el-button type="text" @click="addProject">+ 添加</el-button>
            </div>
            <div class="section-content">
              <div v-for="(project, index) in resumeData.projectExperience" :key="index"
                   class="project-item">
                <el-form :model="project" label-width="80px">
                  <el-form-item label="项目名称">
                    <el-input v-model="project.projectName" placeholder="请输入项目名称" />
                  </el-form-item>
                  <el-form-item label="技术栈">
                    <el-input v-model="project.technologies" placeholder="请输入技术栈" />
                  </el-form-item>
                  <el-form-item label="时间">
                    <el-date-picker
                      v-model="project.startDate"
                      type="date"
                      placeholder="开始时间"
                      style="width: 45%; margin-right: 10px;"
                    />
                    <el-date-picker
                      v-model="project.endDate"
                      type="date"
                      placeholder="结束时间"
                      style="width: 45%;"
                    />
                  </el-form-item>
                  <el-form-item label="描述">
                    <el-input
                      v-model="project.description"
                      type="textarea"
                      :rows="3"
                      placeholder="请输入项目描述"
                    />
                  </el-form-item>
                  <el-form-item>
                    <el-button type="danger" size="small" @click="removeProject(index)">删除
                    </el-button>
                  </el-form-item>
                </el-form>
              </div>
            </div>
          </div>

          <!-- 技能 -->
          <div class="resume-section" v-if="resumeData.skills">
            <div class="section-header">
              <h3>技能</h3>
              <el-button type="text" @click="addSkill">+ 添加</el-button>
            </div>
            <div class="section-content">
              <div v-for="(skill, index) in resumeData.skills" :key="index" class="skill-item">
                <el-form :model="skill" label-width="80px">
                  <el-form-item label="技能名称">
                    <el-input v-model="skill.skillName" placeholder="请输入技能名称" />
                  </el-form-item>
                  <el-form-item label="熟练度">
                    <el-select v-model="skill.skillLevel" placeholder="请选择熟练度">
                      <el-option label="初级" value="初级" />
                      <el-option label="中级" value="中级" />
                      <el-option label="高级" value="高级" />
                      <el-option label="专家" value="专家" />
                    </el-select>
                  </el-form-item>
                  <el-form-item>
                    <el-button type="danger" size="small" @click="removeSkill(index)">删除
                    </el-button>
                  </el-form-item>
                </el-form>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧预览区域 -->
      <div class="editor-right">
        <div class="preview-container">
          <h3>预览效果</h3>
          <div class="preview-content">
            <ResumePreview :resume-data="{ content: resumeData }" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import ResumePreview from '@/components/resume/ResumePreview.vue'
import { addResume } from '@/api/jianliguanli'
import { queryResumeTemplateById } from '@/api/jianlimobanguanli'

const route = useRoute()
const router = useRouter()

// 简历数据
const resumeData = reactive<API.ResumeContent>({
  basicInfo: {
    name: '',
    email: '',
    phone: '',
    address: ''
  },
  education: [],
  workExperience: [],
  projectExperience: [],
  skills: []
})

// 简历设置
const resumeSettings = reactive({
  title: '',
  status: 1, // 默认为草稿
  isPublic: 0 // 默认为私有
})

// 发布状态
const publishing = ref(false)

// 模板ID
const templateId = ref<string>('')

// 初始化页面
onMounted(async () => {
  templateId.value = route.query.templateId as string || ''
  if (templateId.value) {
    await loadTemplate()
  }
})

// 加载模板
const loadTemplate = async () => {
  try {
    const res = await queryResumeTemplateById({ id: templateId.value })
    if (res.data.code === 200 && res.data.data) {
      // 使用模板的默认结构初始化简历数据
      const template = res.data.data
      if (template) {
        Object.assign(resumeData, template)
      }
    }
  } catch (error) {
    console.error('加载模板失败:', error)
    ElMessage.error('加载模板失败')
  }
}

// 添加教育经历
const addEducation = () => {
  resumeData.education?.push({
    schoolName: '',
    major: '',
    degree: '',
    startDate: '',
    endDate: ''
  })
}

// 删除教育经历
const removeEducation = (index: number) => {
  resumeData.education?.splice(index, 1)
}

// 添加工作经验
const addWorkExperience = () => {
  resumeData.workExperience?.push({
    companyName: '',
    position: '',
    startDate: '',
    endDate: '',
    jobDescription: ''
  })
}

// 删除工作经验
const removeWorkExperience = (index: number) => {
  resumeData.workExperience?.splice(index, 1)
}

// 添加项目经历
const addProject = () => {
  resumeData.projectExperience?.push({
    projectName: '',
    technologies: [],
    startDate: '',
    endDate: '',
    description: ''
  })
}

// 删除项目经历
const removeProject = (index: number) => {
  resumeData.projectExperience?.splice(index, 1)
}

// 添加技能
const addSkill = () => {
  resumeData.skills?.push({
    skillName: '',
    skillLevel: undefined
  })
}

// 删除技能
const removeSkill = (index: number) => {
  resumeData.skills?.splice(index, 1)
}

// 格式化日期为 yyyy-MM-dd 格式
const formatDateForBackend = (date: any): string => {
  if (!date) return ''
  if (typeof date === 'string') return date
  if (date instanceof Date) {
    return date.toISOString().split('T')[0]
  }
  return ''
}

// 格式化简历数据中的所有日期字段
const formatResumeDataForBackend = (data: any) => {
  const formattedData = JSON.parse(JSON.stringify(data))

  // 格式化教育经历中的日期
  if (formattedData.education) {
    formattedData.education.forEach((edu: any) => {
      edu.startDate = formatDateForBackend(edu.startDate)
      edu.endDate = formatDateForBackend(edu.endDate)
    })
  }

  // 格式化工作经验中的日期
  if (formattedData.workExperience) {
    formattedData.workExperience.forEach((work: any) => {
      work.startDate = formatDateForBackend(work.startDate)
      work.endDate = formatDateForBackend(work.endDate)
    })
  }

  // 格式化项目经历中的日期
  if (formattedData.projectExperience) {
    formattedData.projectExperience.forEach((project: any) => {
      project.startDate = formatDateForBackend(project.startDate)
      project.endDate = formatDateForBackend(project.endDate)
    })
  }

  return formattedData
}

// 发布简历
const publishResume = async () => {
  try {
    // 验证必填字段
    if (!resumeSettings.title.trim()) {
      ElMessage.warning('请输入简历标题')
      return
    }

    publishing.value = true

    // 格式化日期数据
    const formattedResumeData = formatResumeDataForBackend(resumeData)

    const request: API.ResumeAddRequest = {
      title: resumeSettings.title,
      status: resumeSettings.status,
      isPublic: resumeSettings.isPublic,
      content: formattedResumeData
    }

    const res = await addResume(request)
    if (res.data.code === 200) {
      ElMessage.success('简历创建成功！')
      await router.push('/resume/list') // 跳转到简历列表页面
    } else {
      ElMessage.error(res.data.message || '创建失败')
    }
  } catch (error) {
    console.error('创建简历失败:', error)
    ElMessage.error('创建简历失败')
  } finally {
    publishing.value = false
  }
}
</script>

<style scoped>
.resume-editor {
  height: 100vh;
  width: 100vw;
  display: flex;
  flex-direction: column;
  background-color: #f5f5f5;
  margin: 0;
  padding: 0;
}

.editor-header {
  min-height: 60px;
  background: white;
  border-bottom: 1px solid #e4e7ed;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 24px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.header-left {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.header-left h2 {
  margin: 0;
  color: #303133;
  font-size: 18px;
  font-weight: 500;
}

.header-form {
  display: flex;
  align-items: center;
  gap: 16px;
}

.header-form .el-form-item {
  margin-bottom: 0;
  margin-right: 0;
}

.header-form .el-form-item__label {
  font-size: 12px;
  color: #606266;
  white-space: nowrap;
}

.editor-content {
  flex: 1;
  display: flex;
  gap: 20px;
  padding: 20px;
  overflow: hidden;
  width: 100%;
}

.editor-left {
  flex: 1;
  background: white;
  border-radius: 8px;
  padding: 20px;
  overflow-y: auto;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.editor-right {
  flex: 1;
  background: white;
  border-radius: 8px;
  padding: 20px;
  overflow-y: auto;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.resume-section {
  margin-bottom: 30px;
  border: 1px solid #e4e7ed;
  border-radius: 6px;
  overflow: hidden;
}

.section-header {
  background: #f8f9fa;
  padding: 12px 16px;
  border-bottom: 1px solid #e4e7ed;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.section-header h3 {
  margin: 0;
  font-size: 16px;
  color: #303133;
  font-weight: 500;
}

.section-content {
  padding: 16px;
}

.education-item,
.work-item,
.project-item,
.skill-item {
  margin-bottom: 20px;
  padding: 16px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  background: #fafafa;
}

.education-item:last-child,
.work-item:last-child,
.project-item:last-child,
.skill-item:last-child {
  margin-bottom: 0;
}

.preview-container h3 {
  margin: 0 0 20px 0;
  font-size: 16px;
  color: #303133;
  font-weight: 500;
  text-align: center;
}

.preview-content {
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 20px;
  background: white;
  min-height: 600px;
}

/* 滚动条样式 */
.editor-left::-webkit-scrollbar,
.editor-right::-webkit-scrollbar {
  width: 6px;
}

.editor-left::-webkit-scrollbar-track,
.editor-right::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.editor-left::-webkit-scrollbar-thumb,
.editor-right::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.editor-left::-webkit-scrollbar-thumb:hover,
.editor-right::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>
