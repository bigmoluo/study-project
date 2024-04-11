<script setup>
import {useStore} from "@/stores/index.js";

const store = useStore()

defineProps({
    type: Number
})
</script>

<template>
    <div class="topic-type"
         :style="{
            color: store.findTypeById(type)?.color + 'EE',
            'border-color': store.findTypeById(type)?.color + 'DD',
            'background': store.findTypeById(type)?.color + '22'
        }">
        {{store.findTypeById(type)?.name}}
    </div>
</template>

<style scoped>
.topic-type {
    display: inline-block;
    border:solid 0.5px grey;
    border-radius: 5px;
    font-size: 13px;
    padding: 0 5px;
}
</style>