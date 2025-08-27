import html2canvas from 'html2canvas'
import { nextTick } from 'vue'

/**
 * 生成简历预览图片
 * @param element - 要转换的DOM元素
 * @param options - html2canvas选项
 * @returns Promise<string> - 返回base64格式的图片数据
 */
export async function generatePreviewImage(
  element: HTMLElement,
  options: Partial<html2canvas.Options> = {}
): Promise<string> {
  try {
    // 等待Vue组件完全渲染
    await nextTick()
    
    // 默认配置
    const defaultOptions: Partial<html2canvas.Options> = {
      scale: 2, // 提高图片质量
      useCORS: true, // 允许跨域图片
      allowTaint: true,
      backgroundColor: '#ffffff',
      width: 300, // 固定宽度
      height: 400, // 固定高度
      ...options
    }
    
    const canvas = await html2canvas(element, defaultOptions)
    return canvas.toDataURL('image/png', 0.8)
  } catch (error) {
    console.error('生成预览图片失败:', error)
    throw error
  }
}

/**
 * 创建临时的简历预览元素并生成图片
 * @param resumeData - 简历数据
 * @param templateConfig - 模板配置
 * @returns Promise<string> - 返回base64格式的图片数据
 */
export async function generateResumePreviewImage(
  resumeData: any,
  templateConfig: any
): Promise<string> {
  return new Promise((resolve, reject) => {
    try {
      // 创建临时容器
      const container = document.createElement('div')
      container.style.position = 'absolute'
      container.style.left = '-9999px'
      container.style.top = '-9999px'
      container.style.width = '800px' // 增加宽度以获得更好的渲染效果
      container.style.minHeight = '1000px' // 设置最小高度
      container.style.backgroundColor = '#ffffff'
      container.style.overflow = 'visible' // 允许内容溢出以获取完整高度
      
      document.body.appendChild(container)
      
      // 创建Vue应用实例来渲染ResumePreview组件
      import('@/components/ResumePreview.vue').then(({ default: ResumePreview }) => {
        import('vue').then(({ createApp }) => {
          const app = createApp(ResumePreview, {
            resumeData: { content: resumeData },
            templateConfig: templateConfig
          })
          
          app.mount(container)
          
          // 等待组件渲染完成后生成图片
          setTimeout(async () => {
            try {
              // 获取实际渲染的内容高度
              const actualHeight = container.scrollHeight
              const actualWidth = container.scrollWidth
              
              // 计算缩放比例，确保预览图片比例合适
              const targetWidth = 300
              const targetHeight = Math.min(400, (actualHeight / actualWidth) * targetWidth)
              
              const imageData = await generatePreviewImage(container, {
                width: actualWidth,
                height: actualHeight,
                scale: targetWidth / actualWidth, // 根据目标宽度计算缩放比例
                useCORS: true,
                allowTaint: true,
                backgroundColor: '#ffffff'
              })
              
              // 清理临时元素
              app.unmount()
              document.body.removeChild(container)
              
              resolve(imageData)
            } catch (error) {
              // 清理临时元素
              app.unmount()
              document.body.removeChild(container)
              reject(error)
            }
          }, 1500) // 增加等待时间确保组件完全渲染
        })
      })
    } catch (error) {
      reject(error)
    }
  })
}

/**
 * 预览图片缓存
 */
class PreviewImageCache {
  private cache = new Map<string, string>()
  private maxSize = 50 // 最大缓存数量
  
  /**
   * 生成缓存键
   */
  private generateKey(templateId: string | number, templateConfig: any): string {
    return `${templateId}_${JSON.stringify(templateConfig)}`
  }
  
  /**
   * 获取缓存的预览图片
   */
  get(templateId: string | number, templateConfig: any): string | null {
    const key = this.generateKey(templateId, templateConfig)
    return this.cache.get(key) || null
  }
  
  /**
   * 设置缓存的预览图片
   */
  set(templateId: string | number, templateConfig: any, imageData: string): void {
    const key = this.generateKey(templateId, templateConfig)
    
    // 如果缓存已满，删除最旧的条目
    if (this.cache.size >= this.maxSize) {
      const firstKey = this.cache.keys().next().value
      this.cache.delete(firstKey)
    }
    
    this.cache.set(key, imageData)
  }
  
  /**
   * 清空缓存
   */
  clear(): void {
    this.cache.clear()
  }
}

// 导出缓存实例
export const previewImageCache = new PreviewImageCache()