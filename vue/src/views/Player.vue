<template>
  <div style="padding: 10px">
    <el-card>
      <div v-for="item in videos" :key="item.id"
           style="margin: 10px 0; padding: 10px 0; color: #666; border-bottom: 1px dashed #ccc">
        <span style="font-size: 18px; cursor: pointer" class="item" @click="detail(item.id)">{{ item.name }}</span>
        <span style="float: right; font-size: 12px; margin-top: 10px">文件大小：{{ item.size }} kb</span>
      </div>
    </el-card>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Player",
  data() {
    return {
      videos: [
      ]
    }
  },
  created() {
    request.get("/files").then(res => {
      console.log(res.data.records)
      this.videos = res.data.records.filter(v => v.type === 'mp4')
    })
  },
  methods: {
    detail(id) {
      this.$router.push({path: "/videoDetail", query: {id: id}})
    }
  }
}
</script>

<style scoped>
.item:hover {
  color: #3aBee6;
}
</style>