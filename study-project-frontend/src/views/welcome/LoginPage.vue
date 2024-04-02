<script setup>
import {Lock, User} from "@element-plus/icons-vue";
import {reactive, ref} from "vue";
import {login} from "@/net/indexMethod.js";
import router from "@/router/index.js";

const formRef = ref()

const form = reactive({
    username: '',
    password: '',
    remember: false
})

const rule = {
    username: [
      {required: true, message: '请输入用户名'}
    ],
    password: [
    {required: true, message: '请输入密码'}
  ]
}

function userLogin(){
  formRef.value.validate((valid) => {
    if (valid) {
      login(form.username, form.password, form.remember,
          () => { router.push('/index')})
    }
  })
}


</script>

<template>
    <div style="text-align: center;margin: 0 20px">
      <div style="margin-top: 150px">
        <div style="font-size: 25px;margin-top: 150px;font-weight: bold">登录</div>
        <div style="font-size: 12px;color: gray">在进入系统之前请先输入用户名和密码进行登录</div>
      </div>
      <div style="margin-top: 25px">
          <el-form :model="form" :rules="rule" ref="formRef">
            <el-form-item prop="username">
              <el-input v-model="form.username" type="text" placeholder="用户名/邮箱">
                <template #prefix>
                  <el-icon><User /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="form.password" type="password" style="margin-top: 10px" placeholder="密码">
                <template #prefix>
                  <el-icon><Lock /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            <el-row style="margin-top: 5px">
              <el-col :span="12" style="text-align: left">
                <el-form-item prop="remember">
                  <el-checkbox v-model="form.remember" label="记住我" size="large" />
                </el-form-item>
              </el-col>
              <el-col :span="12" style="text-align: right;margin-top: 6px">
                <el-link>忘记密码？</el-link>
              </el-col>
            </el-row>
            <div style="margin-top: 20px">
              <el-button @click="userLogin" style="width: 270px" type="success" plain>立即登录</el-button>
            </div>
            <el-divider>
              <span style="color: gray">没有账户</span>
            </el-divider>
            <div style="margin-top: 20px">
              <el-button @click="router.push('/register')" style=" width: 270px" type="warning" plain>注册账号</el-button>
            </div>
          </el-form>
      </div>
    </div>
</template>

<style scoped>

</style>