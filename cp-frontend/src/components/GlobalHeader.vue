<template>
  <el-row class="header-row">
    <!-- 左侧 Logo 和网站名称 -->
    <el-col :span="4" class="logo-col">
      <div class="logo">
        <span>CareerPlan</span>
      </div>
    </el-col>

    <!-- 中间菜单项 -->
    <el-col :span="16" class="menu-col">
      <el-menu mode="horizontal" class="menu-container" :router="true">
        <el-menu-item v-for="item in menuItems" :key="item.path" :index="item.path" :route="{ path: item.path }">
          {{ item.label }}
        </el-menu-item>
      </el-menu>
    </el-col>

    <!-- 右侧登录按钮 -->
    <el-col :span="4" class="login-col">
      <el-button type="primary" class="login-btn">登录</el-button>
    </el-col>
  </el-row>
</template>

<script setup lang="ts">
const menuItems = [
  { path: '/', label: '首页' },
  { path: '/assessment', label: '职业测评' }
]
</script>

<style scoped>
.header-row {
  width: 100%;
  display: flex;
  align-items: center;
  background-color: #fff;
  padding: 0 20px;
  box-shadow: 0 0px 2px rgba(0, 0, 0, 0.1);
}

.logo-col {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  min-width: 150px;
}

.logo {
  font-size: 1.5rem;
  font-weight: bold;
  color: #4096ff;
  padding: 10px 0;
}

.menu-col {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.menu-container {
  width: 100%;
  justify-content: center;
}

.login-col {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  min-width: 100px;
}

.login-btn {
  min-width: 80px;
}
</style>


