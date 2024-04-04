<script setup>

import {logout} from "@/net/indexMethod.js";
import router from "@/router/index.js";

function userLogout(){
  logout(() => router.push('/'))
}
</script>

<template>
  <div class="main-content">
    <el-container style="height: 100%">
      <el-header class="main-content-header">
        <el-image class="logo" src="https://element-plus.org/images/element-plus-logo.svg"/>
        <div style="flex: 1" class="user-info">
          <div class="profile">
            <div>用户名</div>
            <div>电子邮箱</div>
          </div>
          <el-avatar src="src/assets/image/5.jpg"/>
        </div>
      </el-header>
      <el-container>
        <el-aside width="200px">
          Aside
          <div>
            <el-button @click="userLogout">退出登录</el-button>
          </div>
        </el-aside>
        <el-main>Main</el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style lang="less" scoped>
.main-content {
  height: 100vh;
  width: 100vw;
}
.main-content-header {
  border-bottom: solid 1px var(--el-bg-color);
  height: 55px;
  display: flex;
  align-items: center;
  box-sizing: border-box;
  .logo {
    height: 32px;
  }
  .user-info {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    .profile {
      text-align: right;
      margin-right: 20px;
      :first-child {
        font-size: 18px;
        font-weight: bold;
        line-height: 20px;
      }
      :last-child {
        font-size: 10px;
        color: gray;
      }
    }
  }
}
</style>