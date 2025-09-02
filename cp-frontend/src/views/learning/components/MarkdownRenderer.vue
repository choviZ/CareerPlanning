<template>
  <div class="markdown-renderer">
    <div 
      ref="markdownContainer"
      class="markdown-content"
      v-html="renderedContent"
    ></div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, nextTick } from 'vue'
import { marked } from 'marked'
import DOMPurify from 'dompurify'
import hljs from 'highlight.js'
import 'highlight.js/styles/github.css'

// 组件属性
interface Props {
  content: string
}

const props = defineProps<Props>()

// 模板引用
const markdownContainer = ref<HTMLElement>()

/**
 * 配置 marked 选项
 */
const configureMarked = () => {
  // 设置 marked 选项
  marked.setOptions({
    highlight: function(code, lang) {
      // 如果指定了语言且 highlight.js 支持，则高亮显示
      if (lang && hljs.getLanguage(lang)) {
        try {
          return hljs.highlight(code, { language: lang }).value
        } catch (err) {
          console.warn('代码高亮失败:', err)
        }
      }
      // 否则进行自动检测
      try {
        return hljs.highlightAuto(code).value
      } catch (err) {
        console.warn('代码高亮失败:', err)
        return code
      }
    },
    langPrefix: 'hljs language-',
    breaks: true, // 支持换行符转换为 <br>
    gfm: true, // 启用 GitHub Flavored Markdown
    tables: true, // 支持表格
    sanitize: false // 关闭内置的 HTML 清理，我们使用 DOMPurify
  })

  // 自定义渲染器
  const renderer = new marked.Renderer()
  
  // 自定义链接渲染，外部链接在新窗口打开
  renderer.link = function(href, title, text) {
    const isExternal = href && (href.startsWith('http') || href.startsWith('//'))
    const target = isExternal ? ' target="_blank" rel="noopener noreferrer"' : ''
    const titleAttr = title ? ` title="${title}"` : ''
    return `<a href="${href}"${target}${titleAttr}>${text}</a>`
  }
  
  // 自定义图片渲染，添加懒加载和错误处理
  renderer.image = function(href, title, text) {
    const titleAttr = title ? ` title="${title}"` : ''
    const altAttr = text ? ` alt="${text}"` : ''
    return `<img src="${href}"${altAttr}${titleAttr} loading="lazy" onerror="this.style.display='none'" />`
  }
  
  // 自定义代码块渲染
  renderer.code = function(code, language) {
    const validLang = language && hljs.getLanguage(language) ? language : 'plaintext'
    const highlightedCode = hljs.highlight(code, { language: validLang }).value
    return `
      <div class="code-block">
        <div class="code-header">
          <span class="code-language">${validLang}</span>
          <button class="copy-button" onclick="copyCode(this)" title="复制代码">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor">
              <path d="M16 1H4c-1.1 0-2 .9-2 2v14h2V3h12V1zm3 4H8c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h11c1.1 0 2-.9 2-2V7c0-1.1-.9-2-2-2zm0 16H8V7h11v14z"/>
            </svg>
          </button>
        </div>
        <pre><code class="hljs language-${validLang}">${highlightedCode}</code></pre>
      </div>
    `
  }
  
  marked.use({ renderer })
}

/**
 * 渲染后的内容
 */
const renderedContent = computed(() => {
  if (!props.content) {
    return '<div class="empty-content">暂无内容</div>'
  }
  
  try {
    // 使用 marked 解析 Markdown
    const rawHtml = marked(props.content)
    
    // 使用 DOMPurify 清理 HTML，防止 XSS 攻击
    const cleanHtml = DOMPurify.sanitize(rawHtml, {
      ALLOWED_TAGS: [
        'h1', 'h2', 'h3', 'h4', 'h5', 'h6',
        'p', 'br', 'strong', 'em', 'u', 's', 'del', 'ins',
        'a', 'img', 'video', 'audio',
        'ul', 'ol', 'li',
        'table', 'thead', 'tbody', 'tr', 'th', 'td',
        'blockquote', 'pre', 'code',
        'div', 'span',
        'hr'
      ],
      ALLOWED_ATTR: [
        'href', 'target', 'rel', 'title', 'alt',
        'src', 'width', 'height', 'loading',
        'class', 'id', 'style',
        'onerror', 'onclick'
      ]
    })
    
    return cleanHtml
  } catch (error) {
    console.error('Markdown 渲染失败:', error)
    return '<div class="error-content">内容渲染失败</div>'
  }
})

/**
 * 添加复制代码功能
 */
const addCopyCodeFunction = () => {
  // 在全局添加复制代码函数
  if (typeof window !== 'undefined') {
    (window as any).copyCode = function(button: HTMLElement) {
      const codeBlock = button.closest('.code-block')
      const codeElement = codeBlock?.querySelector('code')
      if (codeElement) {
        const text = codeElement.textContent || ''
        navigator.clipboard.writeText(text).then(() => {
          const originalText = button.innerHTML
          button.innerHTML = `
            <svg width="16" height="16" viewBox="0 0 24 24" fill="currentColor">
              <path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z"/>
            </svg>
          `
          button.style.color = '#67c23a'
          setTimeout(() => {
            button.innerHTML = originalText
            button.style.color = ''
          }, 2000)
        }).catch(err => {
          console.error('复制失败:', err)
        })
      }
    }
  }
}

// 组件挂载时配置 marked 和添加复制功能
onMounted(() => {
  configureMarked()
  nextTick(() => {
    addCopyCodeFunction()
  })
})
</script>

<style scoped>
.markdown-renderer {
  width: 100%;
}

.markdown-content {
  line-height: 1.6;
  color: #333;
  font-size: 16px;
}

/* 全局样式，不使用 scoped */
</style>

<style>
/* Markdown 内容样式 */
.markdown-content h1,
.markdown-content h2,
.markdown-content h3,
.markdown-content h4,
.markdown-content h5,
.markdown-content h6 {
  margin: 24px 0 16px 0;
  font-weight: 600;
  line-height: 1.25;
  color: #303133;
}

.markdown-content h1 {
  font-size: 2em;
  border-bottom: 2px solid #ebeef5;
  padding-bottom: 8px;
}

.markdown-content h2 {
  font-size: 1.5em;
  border-bottom: 1px solid #ebeef5;
  padding-bottom: 6px;
}

.markdown-content h3 {
  font-size: 1.25em;
}

.markdown-content h4 {
  font-size: 1em;
}

.markdown-content h5 {
  font-size: 0.875em;
}

.markdown-content h6 {
  font-size: 0.85em;
  color: #606266;
}

.markdown-content p {
  margin: 16px 0;
  line-height: 1.6;
}

.markdown-content a {
  color: #409eff;
  text-decoration: none;
}

.markdown-content a:hover {
  text-decoration: underline;
}

.markdown-content strong {
  font-weight: 600;
}

.markdown-content em {
  font-style: italic;
}

.markdown-content ul,
.markdown-content ol {
  margin: 16px 0;
  padding-left: 24px;
}

.markdown-content li {
  margin: 4px 0;
}

.markdown-content blockquote {
  margin: 16px 0;
  padding: 0 16px;
  border-left: 4px solid #dcdfe6;
  background-color: #f8f9fa;
  color: #606266;
}

.markdown-content table {
  width: 100%;
  border-collapse: collapse;
  margin: 16px 0;
  border: 1px solid #ebeef5;
}

.markdown-content th,
.markdown-content td {
  padding: 12px;
  text-align: left;
  border: 1px solid #ebeef5;
}

.markdown-content th {
  background-color: #f5f7fa;
  font-weight: 600;
}

.markdown-content tr:nth-child(even) {
  background-color: #fafafa;
}

.markdown-content img {
  max-width: 100%;
  height: auto;
  margin: 16px 0;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.markdown-content hr {
  margin: 24px 0;
  border: none;
  border-top: 1px solid #ebeef5;
}

/* 代码样式 */
.markdown-content code {
  background-color: #f5f7fa;
  padding: 2px 6px;
  border-radius: 3px;
  font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
  font-size: 0.9em;
  color: #e74c3c;
}

.markdown-content .code-block {
  margin: 16px 0;
  border-radius: 6px;
  overflow: hidden;
  border: 1px solid #ebeef5;
}

.markdown-content .code-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #f5f7fa;
  padding: 8px 16px;
  border-bottom: 1px solid #ebeef5;
}

.markdown-content .code-language {
  font-size: 12px;
  color: #606266;
  font-weight: 500;
  text-transform: uppercase;
}

.markdown-content .copy-button {
  background: none;
  border: none;
  cursor: pointer;
  color: #909399;
  padding: 4px;
  border-radius: 3px;
  transition: all 0.2s;
}

.markdown-content .copy-button:hover {
  background-color: #e1e3e9;
  color: #606266;
}

.markdown-content .code-block pre {
  margin: 0;
  padding: 16px;
  background-color: #fafafa;
  overflow-x: auto;
  font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
  font-size: 14px;
  line-height: 1.5;
}

.markdown-content .code-block code {
  background: none;
  padding: 0;
  color: inherit;
  font-size: inherit;
}

/* 空内容和错误状态 */
.markdown-content .empty-content,
.markdown-content .error-content {
  text-align: center;
  padding: 40px 20px;
  color: #909399;
  font-style: italic;
}

.markdown-content .error-content {
  color: #f56c6c;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .markdown-content {
    font-size: 15px;
  }
  
  .markdown-content h1 {
    font-size: 1.75em;
  }
  
  .markdown-content h2 {
    font-size: 1.4em;
  }
  
  .markdown-content h3 {
    font-size: 1.2em;
  }
  
  .markdown-content .code-block pre {
    padding: 12px;
    font-size: 13px;
  }
  
  .markdown-content table {
    font-size: 14px;
  }
  
  .markdown-content th,
  .markdown-content td {
    padding: 8px;
  }
}
</style>