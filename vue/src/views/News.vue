<template>
  <div style="padding: 10px">
    <!--    功能区域-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add">新增</el-button>
      <el-button type="primary">导入</el-button>
      <el-button type="primary">导出</el-button>
    </div>

    <!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="请输入关键字" style="width: 20%" clearable></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="load">查询</el-button>
    </div>
    <el-table
        :data="tableData"
        border
        stripe
        style="width: 100%">
      <el-table-column
          prop="id"
          label="ID"
          sortable
      >
      </el-table-column>
      <el-table-column
          prop="title"
          label="标题">
      </el-table-column>
      <el-table-column
          prop="author"
          label="作者">
      </el-table-column>
      <el-table-column
          prop="time"
          label="时间">
      </el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button size="mini" @click="details(scope.row)">详情</el-button>
          <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
          <el-popconfirm title="确定删除吗？" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button size="mini" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <div style="margin: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>

      <el-dialog title="提示" v-model="dialogVisible" width="50%">
        <el-form :model="form" label-width="120px">
          <el-form-item label="标题">
            <el-input v-model="form.title" style="width: 50%"></el-input>
          </el-form-item>
          <div id="div1">

          </div>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button>
          </span>
        </template>
      </el-dialog>
      <el-dialog title="详情" v-model="vis" width="50%">
        <el-card>
          <div v-html="detail.content" style="min-height: 100px"></div>
        </el-card>
      </el-dialog>

    </div>
  </div>
</template>

<script>

import E from 'wangeditor'
import request from "@/utils/request";

let editor;

export default {
  name: 'News',
  components: {},
  data() {
    return {
      form: {},
      dialogVisible: false,
      search: '',
      currentPage: 1,
      total: 0,
      pageSize: 10,
      tableData: [],
      vis: false,
      detail: {},
    }
  },
  created() {
    this.load()
  },
  methods: {
    details(row) {
      this.detail = row;
      this.vis = true;
    },
    filesUploadSuccess(res) {
      console.log(res);
      this.form.cover = res.data;
    },
    load() {
      request.get("/news", {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    add() {
      this.dialogVisible = true
      this.form = {}
      this.$nextTick(() => {
        editor = new E("#div1")
        editor.config.uploadImgServer = 'http://' + window.server.filesUploadUrl + ':9090/files/editor/upload'
        editor.config.uploadFileName = "file"
        editor.create()
      })
    },
    save() {
      this.form.content = editor.txt.html() //获取编辑器里面的值，探后赋值到实体当中
      if (this.form.id) {
        request.put("/news", this.form).then(res => {
          console.log(res)
          if (res.code === '200') {
            this.$message({
              type: "success",
              message: "更新成功"
            })
          } else {
            this.$message({
              type: "error",
              message: "res.msg"
            })
          }
        })
        this.load()
        this.dialogVisible = false;
      } else {
        let userStr = sessionStorage.getItem("user") || "{}"
        let user = JSON.parse(userStr);
        this.form.author = user.nickName;
        request.post("/news", this.form).then(res => {
          console.log(res)
          if (res.code === '200') {
            this.$message({
              type: "success",
              message: "新增成功"
            })
          } else {
            this.$message({
              type: "error",
              message: "res.msg"
            })
          }
          this.load()
          this.dialogVisible = false;
        })
      }
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true;
      this.$nextTick(() => {
        editor = new E("#div1")
        editor.config.uploadImgServer = 'http://localhost:9090/files/editor/upload'
        editor.config.uploadFileName = "file"
        editor.create()
        editor.txt.html(row.content)
      })
    },
    handleDelete(id) {
      console.log(id);
      request.delete("/news/" + id).then(res => {
        console.log(id);
        if (res.code === '200') {
          this.$message({
            type: "success",
            message: "删除成功"
          })
        } else {
          this.$message({
            type: "error",
            message: "res.msg"
          })
        }
        this.load()
      })
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.currentPage = pageNum
      this.load()
    }
  }
}
</script>
