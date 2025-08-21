import { defineStore } from 'pinia';
import { ref } from 'vue';
import { getLoginUser } from '@/api/userController';

export const useUserStore = defineStore('user', () => {
  // 用户信息
  const currentUser = ref<API.UserLoginVO>();

  /**
   * 获取当前登录用户信息
   */
  const fetchLoginUser = async () => {
      const res = await getLoginUser();
      if (res.data.code === 200 && res.data.data) {
        currentUser.value = res.data.data;
      }
  };

  /**
   * 清除当前登录用户信息
   */
  const clearLoginState = ()=>{
    currentUser.value = undefined;
  }

  return {
    currentUser,
    fetchLoginUser,
    clearLoginState
  };
});
