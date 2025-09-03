<template>
  <div class="analytics-page">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1 class="page-title">
        <el-icon class="title-icon"><TrendCharts /></el-icon>
        数据统计分析
      </h1>
      <p class="page-subtitle">学习资源数据可视化分析</p>
    </div>

    <!-- 资源概览卡片 -->
    <div class="overview-cards" v-loading="overviewLoading">
      <div class="card-grid">
        <div class="stat-card total">
          <div class="card-icon">
            <el-icon><Files /></el-icon>
          </div>
          <div class="card-content">
            <div class="card-value">{{ resourceOverview?.totalResources || 0 }}</div>
            <div class="card-label">总资源数</div>
          </div>
        </div>

        <div class="stat-card valid">
          <div class="card-icon">
            <el-icon><Select /></el-icon>
          </div>
          <div class="card-content">
            <div class="card-value">{{ resourceOverview?.validResources || 0 }}</div>
            <div class="card-label">有效资源</div>
          </div>
        </div>

        <div class="stat-card deleted">
          <div class="card-icon">
            <el-icon><Delete /></el-icon>
          </div>
          <div class="card-content">
            <div class="card-value">{{ resourceOverview?.deletedResources || 0 }}</div>
            <div class="card-label">已删除</div>
          </div>
        </div>

        <div class="stat-card views">
          <div class="card-icon">
            <el-icon><View /></el-icon>
          </div>
          <div class="card-content">
            <div class="card-value">{{ formatNumber(resourceOverview?.totalViewCount) }}</div>
            <div class="card-label">总浏览量</div>
          </div>
        </div>

        <div class="stat-card avg-views">
          <div class="card-icon">
            <el-icon><TrendCharts /></el-icon>
          </div>
          <div class="card-content">
            <div class="card-value">{{ formatNumber(resourceOverview?.avgViewCount) }}</div>
            <div class="card-label">平均浏览量</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 图表区域 -->
    <div class="charts-container">
      <div class="chart-row">
        <!-- 资源状态分布饼图 -->
        <div class="chart-card">
          <div class="chart-header">
            <h3 class="chart-title">资源状态分布</h3>
            <p class="chart-subtitle">有效资源与已删除资源占比</p>
          </div>
          <div class="chart-content" v-loading="overviewLoading">
            <v-chart
              class="chart"
              :option="resourceStatusOption"
              v-if="resourceOverview"
            />
          </div>
        </div>

        <!-- 类型分布柱状图 -->
        <div class="chart-card">
          <div class="chart-header">
            <h3 class="chart-title">资源类型分布</h3>
            <p class="chart-subtitle">各类型资源数量统计</p>
          </div>
          <div class="chart-content" v-loading="distributionLoading">
            <v-chart
              class="chart"
              :option="typeDistributionOption"
              v-if="typeDistribution.length > 0"
            />
          </div>
        </div>
      </div>

      <div class="chart-row">
        <!-- 类型浏览量对比 -->
        <div class="chart-card full-width">
          <div class="chart-header">
            <h3 class="chart-title">类型浏览量分析</h3>
            <p class="chart-subtitle">各类型资源的总浏览量与平均浏览量对比</p>
          </div>
          <div class="chart-content" v-loading="distributionLoading">
            <v-chart
              class="chart"
              :option="viewAnalysisOption"
              v-if="typeDistribution.length > 0"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { TrendCharts, Files, Select, Delete, View } from '@element-plus/icons-vue'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import {
  CanvasRenderer
} from 'echarts/renderers'
import {
  PieChart,
  BarChart,
  LineChart
} from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
} from 'echarts/components'
import { getResourceOverview, getTypeDistribution } from '@/api/analyseController'

// 注册 ECharts 组件
use([
  CanvasRenderer,
  PieChart,
  BarChart,
  LineChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
])

// 响应式数据
const overviewLoading = ref(false)
const distributionLoading = ref(false)
const resourceOverview = ref<API.ResourceOverviewDTO | null>(null)
const typeDistribution = ref<API.ResourceTypeDistributionDTO[]>([])

// 格式化数字显示
const formatNumber = (num?: number) => {
  if (!num) return '0'
  if (num >= 10000) {
    return (num / 10000).toFixed(1) + 'w'
  }
  if (num >= 1000) {
    return (num / 1000).toFixed(1) + 'k'
  }
  return num.toString()
}

// 资源状态分布饼图配置
const resourceStatusOption = computed(() => {
  if (!resourceOverview.value) return {}

  const data = [
    {
      name: '有效资源',
      value: resourceOverview.value.validResources || 0,
      itemStyle: { color: '#67C23A' }
    },
    {
      name: '已删除资源',
      value: resourceOverview.value.deletedResources || 0,
      itemStyle: { color: '#F56C6C' }
    }
  ]

  return {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      bottom: '5%',
      left: 'center'
    },
    series: [
      {
        name: '资源状态',
        type: 'pie',
        radius: ['40%', '70%'],
        center: ['50%', '45%'],
        avoidLabelOverlap: false,
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 20,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data
      }
    ]
  }
})

// 类型分布柱状图配置
const typeDistributionOption = computed(() => {
  if (typeDistribution.value.length === 0) return {}

  const categories = typeDistribution.value.map(item => item.typeName || '')
  const counts = typeDistribution.value.map(item => item.typeCount || 0)
  const topCounts = typeDistribution.value.map(item => item.topCount || 0)

  return {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    legend: {
      data: ['资源数量', '置顶数量'],
      top: '5%'
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: categories,
      axisLabel: {
        interval: 0,
        rotate: 45
      }
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '资源数量',
        type: 'bar',
        data: counts,
        itemStyle: {
          color: '#409EFF'
        }
      },
      {
        name: '置顶数量',
        type: 'bar',
        data: topCounts,
        itemStyle: {
          color: '#E6A23C'
        }
      }
    ]
  }
})

// 浏览量分析图表配置
const viewAnalysisOption = computed(() => {
  if (typeDistribution.value.length === 0) return {}

  const categories = typeDistribution.value.map(item => item.typeName || '')
  const totalViews = typeDistribution.value.map(item => item.totalView || 0)
  const avgViews = typeDistribution.value.map(item => item.avgView || 0)

  return {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross',
        crossStyle: {
          color: '#999'
        }
      }
    },
    legend: {
      data: ['总浏览量', '平均浏览量'],
      top: '5%'
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: [
      {
        type: 'category',
        data: categories,
        axisPointer: {
          type: 'shadow'
        },
        axisLabel: {
          interval: 0,
          rotate: 45
        }
      }
    ],
    yAxis: [
      {
        type: 'value',
        name: '总浏览量',
        position: 'left',
        axisLabel: {
          formatter: '{value}'
        }
      },
      {
        type: 'value',
        name: '平均浏览量',
        position: 'right',
        axisLabel: {
          formatter: '{value}'
        }
      }
    ],
    series: [
      {
        name: '总浏览量',
        type: 'bar',
        yAxisIndex: 0,
        data: totalViews,
        itemStyle: {
          color: '#67C23A'
        }
      },
      {
        name: '平均浏览量',
        type: 'line',
        yAxisIndex: 1,
        data: avgViews,
        itemStyle: {
          color: '#E6A23C'
        },
        lineStyle: {
          width: 3
        }
      }
    ]
  }
})

// 加载资源概览数据
const loadResourceOverview = async () => {
  overviewLoading.value = true
  try {
    const res = await getResourceOverview()
    if (res.data.code === 200 && res.data.data) {
      resourceOverview.value = res.data.data
    } else {
      ElMessage.error(res.data.message || '获取资源概览失败')
    }
  } catch (error) {
    console.error('获取资源概览失败:', error)
    ElMessage.error('获取资源概览失败')
  } finally {
    overviewLoading.value = false
  }
}

// 加载类型分布数据
const loadTypeDistribution = async () => {
  distributionLoading.value = true
  try {
    const res = await getTypeDistribution()
    if (res.data.code === 200 && res.data.data) {
      typeDistribution.value = res.data.data
    } else {
      ElMessage.error(res.data.message || '获取类型分布失败')
    }
  } catch (error) {
    console.error('获取类型分布失败:', error)
    ElMessage.error('获取类型分布失败')
  } finally {
    distributionLoading.value = false
  }
}

// 页面初始化
onMounted(() => {
  loadResourceOverview()
  loadTypeDistribution()
})
</script>

<style scoped>
.analytics-page {
  padding: 24px;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  min-height: calc(100vh - 60px);
}

.page-header {
  text-align: center;
  margin-bottom: 32px;
  padding: 32px 0;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  backdrop-filter: blur(10px);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

.page-title {
  font-size: 32px;
  font-weight: 700;
  color: #2c3e50;
  margin: 0 0 8px 0;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
}

.title-icon {
  font-size: 36px;
  color: #409EFF;
}

.page-subtitle {
  font-size: 16px;
  color: #7f8c8d;
  margin: 0;
}

.overview-cards {
  margin-bottom: 32px;
}

.card-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 20px;
}

.stat-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
}

.card-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: white;
}

.stat-card.total .card-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-card.valid .card-icon {
  background: linear-gradient(135deg, #67C23A 0%, #85ce61 100%);
}

.stat-card.deleted .card-icon {
  background: linear-gradient(135deg, #F56C6C 0%, #f78989 100%);
}

.stat-card.views .card-icon {
  background: linear-gradient(135deg, #409EFF 0%, #66b1ff 100%);
}

.stat-card.avg-views .card-icon {
  background: linear-gradient(135deg, #E6A23C 0%, #ebb563 100%);
}

.card-content {
  flex: 1;
}

.card-value {
  font-size: 28px;
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 4px;
}

.card-label {
  font-size: 14px;
  color: #7f8c8d;
  font-weight: 500;
}

.charts-container {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.chart-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
}

.chart-row .full-width {
  grid-column: 1 / -1;
}

.chart-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  backdrop-filter: blur(10px);
  transition: all 0.3s ease;
}

.chart-card:hover {
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);
}

.chart-header {
  margin-bottom: 20px;
  text-align: center;
}

.chart-title {
  font-size: 20px;
  font-weight: 600;
  color: #2c3e50;
  margin: 0 0 8px 0;
}

.chart-subtitle {
  font-size: 14px;
  color: #7f8c8d;
  margin: 0;
}

.chart-content {
  position: relative;
  min-height: 300px;
}

.chart {
  width: 100%;
  height: 300px;
}

.full-width .chart {
  height: 400px;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .chart-row {
    grid-template-columns: 1fr;
  }

  .card-grid {
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  }
}

@media (max-width: 768px) {
  .analytics-page {
    padding: 16px;
  }

  .page-title {
    font-size: 24px;
  }

  .card-grid {
    grid-template-columns: 1fr;
  }

  .stat-card {
    padding: 20px;
  }

  .chart-card {
    padding: 20px;
  }
}
</style>
