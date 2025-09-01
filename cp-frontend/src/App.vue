<template>
  <div id="app">
    <div v-if="isUserRoute">
      <FullScreenLayout/>
    </div>
    <div v-else-if="isResumeEdit">
      <FullScreenLayout/>
    </div>
    <div v-else>
      <BasicLayout />
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import BasicLayout from '@/layouts/BasicLayout.vue'
import FullScreenLayout from '@/layouts/UserLayout.vue'

const route = useRoute()
const isUserRoute = computed(() => route.path.startsWith('/user/'))
const isResumeEdit = computed(() => route.path.startsWith('/resume/edit'))
</script>
<style scoped></style>
