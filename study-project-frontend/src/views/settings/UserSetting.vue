<script setup>

import Card from "@/components/Card.vue";
import {Message, Refresh, Select, User} from "@element-plus/icons-vue";
import {useStore} from "@/stores/index.js";
import {computed} from "vue";

const store = useStore

const registerTime = computed(() => new Date(store.user.register_time).toLocaleString())
</script>

<template>
  <div style="display: flex">
    <div class="setting-left">
      <card :icon="User" title="账户信息设置" desc="在这里编辑您的个人信息，您可以在隐式设置中选择是否展示这些信息">
        <el-form label-position="top" style="margin: 0 10px 10px 10px">
          <el-form-item label="用户名">
            <el-input/>
          </el-form-item>
          <el-form-item label="性别">
            <el-radio-group>
              <el-radio label="1">男</el-radio>
              <el-radio label="2">女</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="手机号">
            <el-input/>
          </el-form-item>
          <el-form-item label="QQ号">
            <el-input/>
          </el-form-item>
          <el-form-item label="微信号">
            <el-input/>
          </el-form-item>
          <el-form-item label="个人简介">
            <el-input type="textarea" :rows="6"/>
          </el-form-item>
        </el-form>
        <div>
          <el-button :icon="Select" type="success">保存用户信息</el-button>
        </div>
      </card>
      <card style="margin-top: 10px" :icon="Message" title="电子邮箱设置" desc="您可以在这里修改默认绑定的电子邮箱地址">
        <el-form label-position="top" style="margin: 0 10px 10px 10px">
          <el-form-item label="电子邮箱">
            <el-input/>
          </el-form-item>
          <el-form-item>
            <el-row style="width: 100%" :gutter="10">
              <el-col :span="18">
                <el-input placeholder="请获取验证码"></el-input>
              </el-col>
              <el-col :span="6">
                <el-button type="success" style="width: 100%" plain>获取验证码</el-button>
              </el-col>
            </el-row>
          </el-form-item>
          <div>
            <el-button :icon="Refresh" type="success">保存用户信息</el-button>
          </div>
        </el-form>
      </card>
    </div>
    <div class="setting-right" >
      <div style="position: sticky; top: 20px">
        <card>
          <div style="text-align: center; padding: 5px 15px 0 15px">
              <el-avatar :size="70" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"/>
              <div style="font-weight: bold">你好，{{store.user.username}}</div>
          </div>
          <div style="font-size: 14px; color: gray; padding: 10px">
            我是一名AI助理，专注于提供准确、及时的信息和解决方案。
            擅长语言处理、数据分析，致力于帮助用户提高工作效率，探索知识的海洋
          </div>
        </card>
        <card style="margin-top: 10px; font-size: 14px">
          <div>账户注册时间：{{ registerTime }}</div>
          <div style="color: gray">欢迎加入我们的学习论坛！</div>
        </card>
      </div>
    </div>
  </div>
</template>

<style scoped>
.setting-left {
  flex: 1;
  margin: 20px;
}

.setting-right {
  width: 300px;
  margin: 20px 30px 20px 0;
}
</style>