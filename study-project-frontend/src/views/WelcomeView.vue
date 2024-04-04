<script setup>

</script>

<template>
    <div style="width: 100vw;height: 100vh;overflow: hidden;display: flex">
      <div style="flex: 1">
        <el-image style="width: 100%;height: 100%;" fit="cover"
                  src="https://img2.baidu.com/it/u=2565565015,2965998900&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800"/>
      </div>
      <div class="welcome-title">
        <div style="font-size: 30px;font-weight: bold">欢迎来到我们的学习平台</div>
        <div style="margin-top: 10px">在这里你可以学习到许多后端以及Vue相关的知识，以及Java，如何搭建网站等等技术</div>
        <div style="margin-top: 5px">每天学习一小时，下一个Java之父就是你</div>
      </div>
      <div class="right-card">
        <router-view v-slot="{Component}">
          <transition name="el-fade-in-linear" mode="out-in">
            <component :is="Component"/>
          </transition>
        </router-view>
      </div>

    </div>
</template>

<style scoped>
.welcome-title {
    position: absolute;
    bottom: 30px;
    left: 30px;
    color: white;
    text-shadow: 0 0 10px black;
}
.right-card {
  width: 400px;
  z-index: 1;
  background-color: var(--el-bg-color);
}

</style>