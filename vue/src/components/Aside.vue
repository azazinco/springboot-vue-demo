<template>
  <div>
    <el-menu
        style="width: 200px; min-height: calc(100vh - 50px)"
        :default-active="path"
        router
        class="el-menu-vertical-demo">
      <el-submenu index="1" v-if="user.role === 1">
        <template #title>系统管理</template>
        <el-menu-item index="/user">用户管理</el-menu-item>
      </el-submenu>
      <el-menu-item index="/book">书籍管理</el-menu-item>
      <el-menu-item index="/news" v-if="user.role === 2">新闻管理</el-menu-item>
      <el-menu-item index="/chat">聊天室</el-menu-item>
      <el-menu-item index="/file">文件管理</el-menu-item>
      <el-menu-item index="/player">视频管理</el-menu-item>

    </el-menu>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Aside",
  data() {
    return {
      user: {},
      path: this.$route.path,
    }
  },
  created() {
    let userStr = sessionStorage.getItem("user") || "{}"
    this.user = JSON.parse(userStr)
    // request.get("/user/" + this.user.id).then(res => {
    //   if (res.code === '0') {
    //     this.user.res.data
    //   }
    // })
  }
}
</script>

<style scoped>

</style>