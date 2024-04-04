<script setup>
import {useDark, useToggle} from "@vueuse/core";

useDark({
  selector: 'html',
  attribute: 'class',
  valueDark: 'dark',
  valueLight: 'light'
})

useDark({
  onChanged(light) { useToggle(light)}
})
</script>

<template>
    <router-view/>
</template>

<style scoped>

</style>
