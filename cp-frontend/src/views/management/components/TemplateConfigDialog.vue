<template>
  <el-dialog v-model="visible" title="模板样式配置" width="900px" :close-on-click-modal="false">
    <div class="config-container">
      <el-tabs v-model="activeTab" type="border-card">
        <!-- 布局配置 -->
        <el-tab-pane label="布局设置" name="layout">
          <el-form :model="configData.layout" label-width="120px">
            <el-form-item label="布局类型">
              <el-radio-group v-model="configData.layout.type">
                <el-radio label="single-column">单栏布局</el-radio>
                <el-radio label="two-column">双栏布局</el-radio>
              </el-radio-group>
            </el-form-item>
            <template v-if="configData.layout.type === 'two-column'">
              <el-form-item label="左栏宽度">
                <el-input v-model="configData.layout.leftWidth" placeholder="如：30%">
                  <template #append>%</template>
                </el-input>
              </el-form-item>
              <el-form-item label="右栏宽度">
                <el-input v-model="configData.layout.rightWidth" placeholder="如：70%">
                  <template #append>%</template>
                </el-input>
              </el-form-item>
              <el-form-item label="栏间距">
                <el-input v-model="configData.layout.gap" placeholder="如：20px">
                  <template #append>px</template>
                </el-input>
              </el-form-item>
            </template>
          </el-form>
        </el-tab-pane>

        <!-- 颜色配置 -->
        <el-tab-pane label="颜色主题" name="colors">
          <el-form :model="configData.colors" label-width="120px">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="主色调">
                  <el-color-picker v-model="configData.colors.primary" show-alpha></el-color-picker>
                  <el-input v-model="configData.colors.primary" style="margin-left: 10px; width: 120px;"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="辅助色">
                  <el-color-picker v-model="configData.colors.secondary" show-alpha></el-color-picker>
                  <el-input v-model="configData.colors.secondary" style="margin-left: 10px; width: 120px;"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="强调色">
                  <el-color-picker v-model="configData.colors.accent" show-alpha></el-color-picker>
                  <el-input v-model="configData.colors.accent" style="margin-left: 10px; width: 120px;"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="文字颜色">
                  <el-color-picker v-model="configData.colors.text" show-alpha></el-color-picker>
                  <el-input v-model="configData.colors.text" style="margin-left: 10px; width: 120px;"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="背景色">
                  <el-color-picker v-model="configData.colors.background" show-alpha></el-color-picker>
                  <el-input v-model="configData.colors.background" style="margin-left: 10px; width: 120px;"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="边框色">
                  <el-color-picker v-model="configData.colors.border" show-alpha></el-color-picker>
                  <el-input v-model="configData.colors.border" style="margin-left: 10px; width: 120px;"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-tab-pane>

        <!-- 字体配置 -->
        <el-tab-pane label="字体样式" name="typography">
          <el-form :model="configData.typography" label-width="120px">
            <el-form-item label="字体族">
              <el-select v-model="configData.typography.fontFamily" placeholder="请选择字体">
                <el-option label="微软雅黑" value="'Microsoft YaHei', 'PingFang SC', sans-serif"></el-option>
                <el-option label="宋体" value="'SimSun', serif"></el-option>
                <el-option label="黑体" value="'SimHei', sans-serif"></el-option>
                <el-option label="楷体" value="'KaiTi', serif"></el-option>
                <el-option label="Arial" value="Arial, sans-serif"></el-option>
                <el-option label="Times New Roman" value="'Times New Roman', serif"></el-option>
              </el-select>
            </el-form-item>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="基础字号">
                  <el-input-number v-model="fontSizeNumber" :min="10" :max="24" :step="1"></el-input-number>
                  <span style="margin-left: 5px;">px</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="行高">
                  <el-input-number v-model="lineHeightNumber" :min="1" :max="3" :step="0.1" :precision="1"></el-input-number>
                </el-form-item>
              </el-col>
            </el-row>
            
            <!-- 标题样式 -->
            <el-divider content-position="left">标题样式</el-divider>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="一级标题字号">
                  <el-input-number v-model="h1FontSizeNumber" :min="16" :max="36" :step="1"></el-input-number>
                  <span style="margin-left: 5px;">px</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="一级标题颜色">
                  <el-color-picker v-model="configData.typography.headings.h1.color"></el-color-picker>
                  <el-input v-model="configData.typography.headings.h1.color" style="margin-left: 10px; width: 120px;"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="二级标题字号">
                  <el-input-number v-model="h2FontSizeNumber" :min="14" :max="28" :step="1"></el-input-number>
                  <span style="margin-left: 5px;">px</span>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="二级标题颜色">
                  <el-color-picker v-model="configData.typography.headings.h2.color"></el-color-picker>
                  <el-input v-model="configData.typography.headings.h2.color" style="margin-left: 10px; width: 120px;"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-tab-pane>

        <!-- 模块配置 -->
        <el-tab-pane label="模块设置" name="sections">
          <el-form label-width="120px">
            <!-- 基本信息模块 -->
            <el-card class="section-card">
              <template #header>
                <span>基本信息模块</span>
              </template>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="位置">
                    <el-radio-group v-model="configData.sections.basicInfo.position">
                      <el-radio label="left">左侧</el-radio>
                      <el-radio label="right">右侧</el-radio>
                      <el-radio label="top">顶部</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="背景色">
                    <el-color-picker v-model="configData.sections.basicInfo.backgroundColor"></el-color-picker>
                    <el-input v-model="configData.sections.basicInfo.backgroundColor" style="margin-left: 10px; width: 120px;"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="内边距">
                    <el-input v-model="configData.sections.basicInfo.padding" placeholder="如：20px">
                      <template #append>px</template>
                    </el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="圆角">
                    <el-input v-model="configData.sections.basicInfo.borderRadius" placeholder="如：8px">
                      <template #append>px</template>
                    </el-input>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-card>

            <!-- 教育经历模块 -->
            <el-card class="section-card">
              <template #header>
                <span>教育经历模块</span>
              </template>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="位置">
                    <el-radio-group v-model="configData.sections.education.position">
                      <el-radio label="left">左侧</el-radio>
                      <el-radio label="right">右侧</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="显示时间线">
                    <el-switch v-model="configData.sections.education.showTimeline"></el-switch>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-form-item label="时间线颜色" v-if="configData.sections.education.showTimeline">
                <el-color-picker v-model="configData.sections.education.timelineColor"></el-color-picker>
                <el-input v-model="configData.sections.education.timelineColor" style="margin-left: 10px; width: 120px;"></el-input>
              </el-form-item>
            </el-card>

            <!-- 技能模块 -->
            <el-card class="section-card">
              <template #header>
                <span>技能模块</span>
              </template>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="位置">
                    <el-radio-group v-model="configData.sections.skills.position">
                      <el-radio label="left">左侧</el-radio>
                      <el-radio label="right">右侧</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="显示类型">
                    <el-select v-model="configData.sections.skills.displayType">
                      <el-option label="列表" value="list"></el-option>
                      <el-option label="进度条" value="progress"></el-option>
                      <el-option label="标签" value="tags"></el-option>
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
              <template v-if="configData.sections.skills.displayType === 'progress'">
                <el-row :gutter="20">
                  <el-col :span="12">
                    <el-form-item label="进度条颜色">
                      <el-color-picker v-model="configData.sections.skills.progressColor"></el-color-picker>
                      <el-input v-model="configData.sections.skills.progressColor" style="margin-left: 10px; width: 120px;"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="进度条高度">
                      <el-input v-model="configData.sections.skills.progressHeight" placeholder="如：8px">
                        <template #append>px</template>
                      </el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
              </template>
            </el-card>
          </el-form>
        </el-tab-pane>

        <!-- JSON编辑 -->
        <el-tab-pane label="JSON编辑" name="json">
          <div class="json-editor">
            <el-input
              v-model="jsonString"
              type="textarea"
              :rows="20"
              placeholder="请输入JSON配置"
            ></el-input>
            <div class="json-actions">
              <el-button @click="formatJson">格式化</el-button>
              <el-button @click="validateJson">验证</el-button>
              <el-button type="primary" @click="applyJson">应用</el-button>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
    
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleCancel">取消</el-button>
        <el-button type="primary" @click="handleConfirm">确认</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'

interface Props {
  modelValue: boolean
  config: Record<string, any>
}

interface Emits {
  (e: 'update:modelValue', value: boolean): void
  (e: 'confirm', config: Record<string, any>): void
}

const props = withDefaults(defineProps<Props>(), {
  modelValue: false,
  config: () => ({})
})

const emit = defineEmits<Emits>()

const visible = computed({
  get: () => props.modelValue,
  set: (value: boolean) => emit('update:modelValue', value)
})

const activeTab = ref('layout')

// 默认配置
const defaultConfig = {
  layout: {
    type: 'single-column',
    leftWidth: '30%',
    rightWidth: '70%',
    gap: '20px'
  },
  colors: {
    primary: '#2c3e50',
    secondary: '#3498db',
    accent: '#e74c3c',
    text: '#2c3e50',
    background: '#ffffff',
    border: '#ecf0f1'
  },
  typography: {
    fontFamily: "'Microsoft YaHei', 'PingFang SC', sans-serif",
    fontSize: '14px',
    lineHeight: '1.6',
    headings: {
      h1: {
        fontSize: '24px',
        fontWeight: 'bold',
        color: '#2c3e50'
      },
      h2: {
        fontSize: '18px',
        fontWeight: '600',
        color: '#3498db'
      }
    }
  },
  sections: {
    basicInfo: {
      position: 'left',
      backgroundColor: '#f8f9fa',
      padding: '20px',
      borderRadius: '8px'
    },
    education: {
      position: 'right',
      showTimeline: true,
      timelineColor: '#3498db'
    },
    skills: {
      position: 'left',
      displayType: 'progress',
      progressColor: '#3498db',
      progressHeight: '8px'
    }
  }
}

// 配置数据
const configData = ref<any>({})

// 字体大小数字值（用于input-number组件）
const fontSizeNumber = computed({
  get: () => parseInt(configData.value.typography?.fontSize || '14'),
  set: (value: number) => {
    if (!configData.value.typography) configData.value.typography = {}
    configData.value.typography.fontSize = `${value}px`
  }
})

const lineHeightNumber = computed({
  get: () => parseFloat(configData.value.typography?.lineHeight || '1.6'),
  set: (value: number) => {
    if (!configData.value.typography) configData.value.typography = {}
    configData.value.typography.lineHeight = value.toString()
  }
})

const h1FontSizeNumber = computed({
  get: () => parseInt(configData.value.typography?.headings?.h1?.fontSize || '24'),
  set: (value: number) => {
    if (!configData.value.typography) configData.value.typography = {}
    if (!configData.value.typography.headings) configData.value.typography.headings = {}
    if (!configData.value.typography.headings.h1) configData.value.typography.headings.h1 = {}
    configData.value.typography.headings.h1.fontSize = `${value}px`
  }
})

const h2FontSizeNumber = computed({
  get: () => parseInt(configData.value.typography?.headings?.h2?.fontSize || '18'),
  set: (value: number) => {
    if (!configData.value.typography) configData.value.typography = {}
    if (!configData.value.typography.headings) configData.value.typography.headings = {}
    if (!configData.value.typography.headings.h2) configData.value.typography.headings.h2 = {}
    configData.value.typography.headings.h2.fontSize = `${value}px`
  }
})

// JSON字符串
const jsonString = ref('')

// 深度合并对象
const deepMerge = (target: any, source: any): any => {
  const result = { ...target }
  for (const key in source) {
    if (source[key] && typeof source[key] === 'object' && !Array.isArray(source[key])) {
      result[key] = deepMerge(result[key] || {}, source[key])
    } else {
      result[key] = source[key]
    }
  }
  return result
}

// 初始化配置
const initConfig = () => {
  configData.value = deepMerge(defaultConfig, props.config || {})
  jsonString.value = JSON.stringify(configData.value, null, 2)
}

// 监听配置变化
watch(() => props.config, initConfig, { immediate: true, deep: true })

// 监听visible变化
watch(visible, (newVal) => {
  if (newVal) {
    initConfig()
  }
})

// 格式化JSON
const formatJson = () => {
  try {
    const parsed = JSON.parse(jsonString.value)
    jsonString.value = JSON.stringify(parsed, null, 2)
    ElMessage.success('JSON格式化成功')
  } catch (error) {
    ElMessage.error('JSON格式错误')
  }
}

// 验证JSON
const validateJson = () => {
  try {
    JSON.parse(jsonString.value)
    ElMessage.success('JSON格式正确')
  } catch (error) {
    ElMessage.error('JSON格式错误')
  }
}

// 应用JSON
const applyJson = () => {
  try {
    const parsed = JSON.parse(jsonString.value)
    configData.value = parsed
    ElMessage.success('JSON配置已应用')
  } catch (error) {
    ElMessage.error('JSON格式错误，无法应用')
  }
}

// 取消
const handleCancel = () => {
  visible.value = false
}

// 确认
const handleConfirm = () => {
  // 同步JSON字符串到配置数据
  if (activeTab.value === 'json') {
    try {
      configData.value = JSON.parse(jsonString.value)
    } catch (error) {
      ElMessage.error('JSON格式错误，请检查后重试')
      return
    }
  } else {
    // 同步配置数据到JSON字符串
    jsonString.value = JSON.stringify(configData.value, null, 2)
  }
  
  emit('confirm', configData.value)
}

// 监听配置数据变化，同步到JSON
watch(configData, () => {
  if (activeTab.value !== 'json') {
    jsonString.value = JSON.stringify(configData.value, null, 2)
  }
}, { deep: true })
</script>

<style scoped>
.config-container {
  height: 500px;
}

.section-card {
  margin-bottom: 20px;
}

.section-card:last-child {
  margin-bottom: 0;
}

.json-editor {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.json-actions {
  margin-top: 10px;
  text-align: right;
}

.json-actions .el-button {
  margin-left: 10px;
}

:deep(.el-tabs__content) {
  height: 450px;
  overflow-y: auto;
}

:deep(.el-color-picker) {
  vertical-align: middle;
}
</style>