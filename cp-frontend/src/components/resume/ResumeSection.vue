<template>
  <div class="resume-section" :style="sectionStyle">
    <h3 class="section-title" :style="titleStyle">
      <i v-if="config?.icon" :class="config.icon" class="section-icon"></i>
      {{ title }}
    </h3>
    <div class="section-content" :style="contentStyle">
      <slot></slot>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

interface Props {
  title: string
  config?: {
    icon?: string
    titleColor?: string
    titleSize?: string
    backgroundColor?: string
    borderColor?: string
    position?: 'left' | 'center' | 'right'
    displayType?: 'list' | 'progress' | 'tags'
    progressColor?: string
    progressHeight?: string
  }
}

const props = defineProps<Props>()

// 计算样式
const sectionStyle = computed(() => {
  const config = props.config || {}
  return {
    backgroundColor: config.backgroundColor || 'transparent',
    borderLeft: config.borderColor ? `3px solid ${config.borderColor}` : 'none',
    paddingLeft: config.borderColor ? '15px' : '0',
    marginBottom: '20px'
  }
})

const titleStyle = computed(() => {
  const config = props.config || {}
  return {
    color: config.titleColor || '#2c3e50',
    fontSize: config.titleSize || '18px',
    textAlign: config.position || 'left',
    margin: '0 0 15px 0',
    fontWeight: 'bold',
    display: 'flex',
    alignItems: 'center',
    gap: '8px'
  }
})

const contentStyle = computed(() => {
  return {
    paddingLeft: '0'
  }
})
</script>

<style scoped>
.resume-section {
  margin-bottom: 25px;
}

.section-title {
  position: relative;
  padding-bottom: 8px;
  border-bottom: 1px solid #e9ecef;
}

.section-icon {
  font-size: 16px;
  color: inherit;
}

.section-content {
  margin-top: 15px;
}
</style>