<template>
    <div class="user-layout">
        <router-view />
    </div>
</template>

<script setup lang="ts">
// No specific script logic needed for a basic layout wrapper
</script>

<style scoped>
.user-layout {
    min-height: 100vh;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: #f0f2f5;
}
</style>
