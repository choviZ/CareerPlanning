<template>
  <BasicLayout v-if="!isUserRoute" />
  <UserLayout v-else />
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import BasicLayout from '@/layouts/BasicLayout.vue';
import UserLayout from '@/layouts/UserLayout.vue';

const route = useRoute();
const isUserRoute = computed(() => route.path.startsWith('/user/'));
</script>
<style scoped></style>
