<template>
  <div>
    <el-form ref="form" :model="form" label-width="80px">
      <el-form-item label="用户id">
        <el-input v-model="form.id"></el-input>
      </el-form-item>
      <el-form-item label="用户名">
        <el-input v-model="form.username"></el-input>
      </el-form-item>
      <el-form-item label="密码">
        <el-input v-model="form.password"></el-input>
      </el-form-item>
      <el-form-item label="昵称">
        <el-input v-model="form.nickName"></el-input>
      </el-form-item>
      <el-form-item label="年龄">
        <el-input v-model="form.age"></el-input>
      </el-form-item>
      <el-form-item label="性别">
        <el-input v-model="form.sex"></el-input>
      </el-form-item>
      <el-form-item label="地址">
        <el-input v-model="form.address"></el-input>
      </el-form-item>
    </el-form>
    <div><el-button @click="update">保存</el-button></div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Person",
  data() {
    return {
      form: {},
    }
  },
  created() {
    let str = sessionStorage.getItem("user");
    this.form = JSON.parse(str);
  },
  methods: {
    update() {
      request.put("/user", this.form).then(res => {
        if (res.code === '200') {
          this.$message({
            type: "success",
            message: "更新成功"
          })
          sessionStorage.setItem("user", JSON.stringify(res.data))
        } else {
          this.$message({
            type: "error",
            message: "更新失败"
          })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>