
import { createApp } from 'vue'
import { createPinia } from 'pinia'

import 'element-plus/dist/index.css'
import 'element-plus/theme-chalk/dark/css-vars.css'

import '@/assets/quill.css'

import App from './App.vue'
import router from './router'
import axios from "axios";


const app = createApp(App)

axios.defaults.baseURL = 'http://localhost:8000'

app.use(createPinia())
app.use(router)

app.mount('#app')
