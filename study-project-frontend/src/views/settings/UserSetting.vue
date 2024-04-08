<script setup>

import Card from "@/components/Card.vue";
import {Message, Refresh, Select, User} from "@element-plus/icons-vue";
import {useStore} from "@/stores/index.js";
import {computed, reactive, ref} from "vue";
import {accessHeader, get, post} from "@/net/indexMethod.js";
import {ElMessage} from "element-plus";
import axios from "axios";

const store = useStore()
const baseFormRef = ref()
const emailFormRef = ref()
const desc = ref('')

const registerTime = computed(() => new Date(store.user.register_time).toLocaleString())

const baseForm = reactive({
    username: '',
    gender: '',
    phone: '',
    qq: '',
    wx: '',
    desc: ''
})

const emailForm = reactive({
    email: '',
    code: ''
})

const validateUsername = (rule, value, callback) => {
    if (value === '') {
        callback(new Error('请输入用户名'))
    } else if (!/^[a-zA-Z0-9\u4e00-\u9fa5]+$/.test(value)) {
        callback(new Error('用户名不能包含特殊字符，只能是中/英文'))
    } else {
        callback()
    }
}

const rules = {
    username: [
        {validator: validateUsername, trigger: ['blur', 'change']}
    ],
    email: [
        {required: true, message: '请输入邮件地址', trigger: 'blur'},
        {type: 'email', message: '请输入合法的电子邮箱', trigger: ['blur', 'change']}
    ]
}

const loading = reactive({
    form: true,
    base: false
})

function saveDetails() {
    baseFormRef.value.validate(isValid => {
        if (isValid) {
            loading.base = true
            post('/api/user/save-details', baseForm, () => {
                ElMessage.success('用户信息保存成功')
                store.user.username = baseForm.username
                desc.value = baseForm.desc
                loading.base = false
            }, (message) => {
                ElMessage.warning(message)
                loading.base = false
            })
        }
    })
}

get('/api/user/details', (data) => {
    baseForm.username = store.user.username
    baseForm.gender = data.gender
    baseForm.phone = data.phone
    baseForm.qq = data.qq
    baseForm.wx = data.wx
    baseForm.desc = desc.value = data.desc
    emailForm.email = store.user.email
    loading.form = false
})

const coldTime = ref(0)
const isEmailValid = ref(true)
const onValidate = (prop, isValid) => {
    if (prop === 'email')
        isEmailValid.value = isValid
}

function sendEmailCode() {
    emailFormRef.value.validate(isValid => {
        if (isValid) {
            coldTime.value = 60
            get(`api/auth/ask-code?email=${emailForm.email}&type=modify`, () => {
                ElMessage.success(`验证码已发送到邮箱：${emailForm.email}，请注意接收`)
                const handle = setInterval(() => {
                    coldTime.value--
                    if (coldTime.value === 0) {
                        clearInterval(handle)
                    }
                }, 1000)
            }, (message) => {
                ElMessage.warning(message)
                coldTime.value = 0
            })
        }
    })
}

function modifyEmail() {
    emailFormRef.value.validate(isValid => {
        if (isValid) {
            post('api/user/modify-email', emailForm, () => {
                ElMessage.success('邮件修改成功')
                store.user.email = emailForm.email
                emailForm.code = ''
            })
        }
    })
}

function beforeAvatarUpload(rawFile) {
    if (rawFile.type !== 'image/jpeg' && rawFile.type !== 'image/png') {
        ElMessage.error('头像只能是 JPG/PNG 格式的')
        return false
    } else if (rawFile.size / 1024 > 1024) {
        ElMessage.error('头像大小不能大于 1024KB')
        return false
    }
    return true
}

function uploadSuccess(response) {
    ElMessage.success('头像上传成功')
    store.user.avatar = response.data
}
</script>

<template>
    <div style="display: flex; max-width: 1050px; margin: auto">
        <div class="setting-left">
            <card :icon="User" title="账户信息设置" v-loading="loading.form"
                  desc="在这里编辑您的个人信息，您可以在隐式设置中选择是否展示这些信息">
                <el-form :model="baseForm" :rules="rules" ref="baseFormRef" label-position="top"
                         style="margin: 0 10px 10px 10px">
                    <el-form-item label="用户名" prop="username">
                        <el-input v-model="baseForm.username" maxlength="10"></el-input>
                    </el-form-item>
                    <el-form-item label="性别" prop="gender">
                        <el-radio-group v-model="baseForm.gender">
                            <el-radio :label="0">男</el-radio>
                            <el-radio :label="1">女</el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="手机号" prop="phone">
                        <el-input v-model="baseForm.phone" maxlength="11"></el-input>
                    </el-form-item>
                    <el-form-item label="QQ号" prop="qq">
                        <el-input v-model="baseForm.qq" maxlength="13"></el-input>
                    </el-form-item>
                    <el-form-item label="微信号" prop="wx">
                        <el-input v-model="baseForm.wx" maxlength="20"></el-input>
                    </el-form-item>
                    <el-form-item label="个人简介" prop="desc">
                        <el-input type="textarea" :rows="6" v-model="baseForm.desc" maxlength="200"></el-input>
                    </el-form-item>
                </el-form>
                <div>
                    <el-button @click="saveDetails" :loading="loading.base" :icon="Select" type="success">保存用户信息
                    </el-button>
                </div>
            </card>
            <card style="margin-top: 10px" :icon="Message" title="电子邮箱设置"
                  desc="您可以在这里修改默认绑定的电子邮箱地址">
                <el-form :model="emailForm" :rules="rules" @validate="onValidate" ref="emailFormRef"
                         label-position="top"
                         style="margin: 0 10px 10px 10px">
                    <el-form-item label="电子邮箱" prop="email">
                        <el-input v-model="emailForm.email"></el-input>
                    </el-form-item>
                    <el-form-item prop="code">
                        <el-row style="width: 100%" :gutter="10">
                            <el-col :span="18">
                                <el-input v-model="emailForm.code" maxlength="6" placeholder="请获取验证码"></el-input>
                            </el-col>
                            <el-col :span="6">
                                <el-button @click="sendEmailCode" :disabled="!isEmailValid || coldTime > 0"
                                           type="success" style="width: 100%" plain>
                                    {{ coldTime > 0 ? `请稍后 ${coldTime} 秒` : '获取验证码' }}
                                </el-button>
                            </el-col>
                        </el-row>
                    </el-form-item>
                    <div>
                        <el-button :icon="Refresh" @click="modifyEmail" type="success">更新电子邮件</el-button>
                    </div>
                </el-form>
            </card>
        </div>
        <div class="setting-right">
            <div style="position: sticky; top: 20px">
                <card>
                    <div style="text-align: center; padding: 5px 15px 0 15px">
                        <el-avatar :size="70"
                                   :src="store.avatarUrl"/>
                        <div style="margin: 5px 0">
                            <el-upload
                                :action="axios.defaults.baseURL + '/api/image/avatar'"
                                :show-file-list="false"
                                :before-upload="beforeAvatarUpload"
                                :on-success="uploadSuccess"
                                :headers="accessHeader()">
                            <el-button size="small" round>修改头像</el-button>
                            </el-upload>
                        </div>
                        <div style="font-weight: bold">你好，{{ store.user.username }}</div>
                    </div>
                    <div style="font-size: 14px; color: gray; padding: 10px">
                        {{ desc || '这个用户很懒，没有填写用户简介~' }}
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