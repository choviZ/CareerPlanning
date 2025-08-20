<template>
  <el-container class="layout-container">
    <el-header><GlobalHeader /></el-header>
    <el-main><router-view /></el-main>
    <el-footer class="footer">© 2025 Career Planning</el-footer>
  </el-container>
</template>

<style scoped>
.layout-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.footer {
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f7fa;
  color: #606266;
  padding: 20px 0;
  border-top: 1px solid #e4e7ed;
}
</style>

<script setup lang="ts">
import GlobalHeader from '@/components/GlobalHeader.vue'
</script>
