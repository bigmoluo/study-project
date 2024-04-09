<script setup>

import LightCard from "@/components/LightCard.vue";
import {CollectionTag, EditPen, Link, MostlyCloudy} from "@element-plus/icons-vue";
import Weather from "@/components/Weather.vue";
import {computed, reactive} from "vue";
import {get} from "@/net/indexMethod.js";
import {ElMessage} from "element-plus";

const weather = reactive({
    location: {},
    now: {},
    hourly: {},
    success: false
})

const today = computed(() => {
    const date = new Date()
    return `${date.getFullYear()} 年 ${date.getMonth()} 月 ${date.getDay()} 日`
})

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
    <div style="display: flex; margin: 20px auto;gap: 20px; max-width: 900px">
        <div style="flex: 1">
            <light-card>
                <div class="create-topic">
                    <el-icon><EditPen/></el-icon>
                     点击发表主题...
                </div>
            </light-card>
            <light-card style="margin-top: 10px; height: 50px">

            </light-card>
            <div style="margin-top: 10px; display: flex;flex-direction: column;gap: 10px">
                <light-card style="height: 150px" v-for="item in 10">

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
    </div>
</template>

<style lang="less" scoped>
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
    background-color: darkgray;
    border-radius: 5px;
    height: 40px;
    font-size: 14px;
    line-height: 40px;
    padding: 0 10px;

    &:hover {
        cursor: pointer;
    }
}
</style>