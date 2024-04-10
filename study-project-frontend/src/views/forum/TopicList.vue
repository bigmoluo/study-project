<script setup>

import LightCard from "@/components/LightCard.vue";
import {Clock, CollectionTag, EditPen, Link, MostlyCloudy} from "@element-plus/icons-vue";
import Weather from "@/components/Weather.vue";
import {computed, reactive, ref} from "vue";
import {get} from "@/net/indexMethod.js";
import {ElMessage} from "element-plus";
import TopicEditor from "@/components/TopicEditor.vue";
import {useStore} from "@/stores/index.js";

const weather = reactive({
    location: {},
    now: {},
    hourly: {},
    success: false
})

const editor = ref(false)
const list = ref(null)
const store = useStore()

const today = computed(() => {
    const date = new Date()
    return `${date.getFullYear()} 年 ${date.getMonth() + 1} 月 ${date.getDate()} 日`
})

get('/api/forum/types', data => store.forum.types = data)
function updateList() {
    get('/api/forum/list-topic?page=0&type=0', data => list.value = data)
}
updateList()

navigator.geolocation.getCurrentPosition( position => {
    const longitude = position.coords.longitude
    const latitude = position.coords.latitude
    get(`/api/forum/weather?longitude=${longitude}&latitude=${latitude}`, data => {
        Object.assign(weather, data)
        weather.success = true;
    })
}, error => {
    console.info(error)
    ElMessage.warning('位置信息获取超时，请检测网络设置')
    get('api/forum/weather?longitude=116.40529&latitude=39.90499', data => {
        Object.assign(weather, data)
        weather.success = true;
    })
}, {
    timeout: 3000,
    enableHighAccuracy: true
})
</script>

<template>
    <div style="display: flex; margin: 20px auto;gap: 20px; max-width: 950px">
        <div style="flex: 1">
            <light-card>
                <div class="create-topic" @click="editor = true">
                    <el-icon><EditPen/></el-icon>
                     点击发表主题...
                </div>
            </light-card>
            <light-card style="margin-top: 10px; height: 50px">

            </light-card>
            <div style="margin-top: 10px; display: flex;flex-direction: column;gap: 10px">
                <light-card v-for="item in list" class="topic-card">
                    <div style="display: flex">
                        <div>
                            <el-avatar :size="35" :src="store.avatarUrl"/>
                        </div>
                        <div style="margin-left: 8px; transform: translateY(4px)">
                            <div style="font-size: 15px; font-weight: bold">{{item.username}}</div>
                            <div style="font-size: 13px; color: grey">
                                <el-icon><Clock/></el-icon>
                                <div style="margin-left: 3px; display: inline-block; transform: translateY(-2px)">
                                    {{new Date(item.time).toLocaleString()}}
                                </div>
                            </div>
                        </div>
                    </div>
                    <div style="margin-top: 5px">
                        <div class="topic-type"
                             :style="{
                                color: store.findTypeById(item.type)?.color + 'EE',
                                'border-color': store.findTypeById(item.type)?.color + 'DD',
                                'background': store.findTypeById(item.type)?.color + '22'
                             }">
                            {{store.findTypeById(item.type)?.name}}
                        </div>
                        <span style="font-weight: bold; margin-left: 5px;">{{item.title}}</span>
                    </div>
                    <div class="topic-content">{{item.text}}</div>
                    <div style="display: grid; grid-template-columns: repeat(3, 1fr); grid-gap: 10px">
                        <el-image class="topic-image" v-for="img in item.images" :src="img" fit="cover"></el-image>
                    </div>
                </light-card>
            </div>
        </div>
        <div style="width: 280px">
            <div style="position: sticky; top: 20px">
                <light-card>
                    <div style="font-weight: bold;">
                        <el-icon><CollectionTag/></el-icon>
                        论坛公告
                    </div>
                    <el-divider style="margin: 10px 0"/>
                    <div style="font-size: 14px; margin: 10px; color: grey">
                        牛牛牛牛牛牛牛牛牛牛牛牛牛牛牛牛牛牛
                        牛牛牛牛牛牛牛牛牛牛牛牛牛牛牛牛牛牛牛牛牛牛牛牛牛牛
                    </div>
                </light-card>
                <light-card style="margin-top: 10px">
                    <div style="font-weight: bold">
                        <el-icon><MostlyCloudy/></el-icon>
                        天气信息
                    </div>
                    <el-divider style="margin: 10px 0"/>
                    <weather :data="weather"/>
                </light-card>
                <light-card style="margin-top: 10px">
                    <div class="info-text">
                        <div>当前日期</div>
                        <div>{{today}}</div>
                    </div>
                    <div class="info-text">
                        <div>当前IP地址</div>
                        <div>127.0.0.1</div>
                    </div>
                </light-card>
                <div style="font-size: 14px; margin-top: 10px; color: grey">
                    <el-icon><Link/></el-icon>
                    友情链接
                    <el-divider style="margin: 10px 0"/>
                </div>
                <div style="display: grid; grid-template-columns: repeat(2, 1fr); grid-gap: 10px; margin-top: 10px">
                    <div class="frient-link">
                        <el-image style="height: 100%" src="https://element-plus.org/images/js-design-banner.jpg"/>
                    </div>
                    <div class="frient-link">
                        <el-image style="height: 100%" src="https://element-plus.org/images/sponsors/vform-banner.png"/>
                    </div>
                    <div class="frient-link">
                        <el-image style="height: 100%" src="https://element-plus.org/images/sponsors/jnpfsoft.jpg"/>
                    </div>
                </div>
            </div>
        </div>
        <TopicEditor :show="editor" @close="editor = false" @success="editor = false;updateList()"/>
    </div>
</template>

<style lang="less" scoped>
.topic-card {
    padding: 15px;
    transition: scale .3s;
    &:hover {
        scale: 1.015;
        cursor: pointer;
    }
    .topic-content {
        font-size: 14px;
        color: grey;
        margin: 5px 0;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 3;
        overflow: hidden;
        text-overflow: ellipsis;
    }
    .topic-type {
        display: inline-block;
        border:solid 0.5px grey;
        border-radius: 5px;
        font-size: 13px;
        padding: 0 5px;
    }
    .topic-image {
        width: 100%;
        height: 100%;
        max-height: 110px;
        border-radius: 5px;
    }
}
.info-text {
    display: flex;
    justify-content: space-between;
    color: grey;
    font-size: 14px;
}
.frient-link {
    border-radius: 5px;
    overflow: hidden;
}
.create-topic {
    background-color: #ececf3;
    border-radius: 5px;
    height: 40px;
    font-size: 14px;
    line-height: 40px;
    padding: 0 10px;

    &:hover {
        cursor: pointer;
    }
}
.dark .create-topic {
    background-color: #323233;
}
</style>