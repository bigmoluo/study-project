<script setup>

import {logout} from "@/net/indexMethod.js";
import router from "@/router/index.js";

function userLogout(){
  logout(() => router.push('/'))
}
</script>

<template>
    <div>欢迎进入学习平台</div>
    <el-button type="danger" @click="userLogout">退出登录</el-button>
</template>

<style scoped>

</style>