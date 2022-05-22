package com.example.demo.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.common.Result;
import com.example.demo.entity.Files;
import com.example.demo.mapper.FilesMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.IFilesService;
import org.apache.ibatis.annotations.Delete;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FilesController {
    @Value("${server.port}")
    private String port;

    @Value("${server.ip}")
    private String serverIp;

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Resource
    IFilesService filesService;

    @PostMapping("/upload")
    public Result upload(@RequestParam MultipartFile file) throws IOException {
        return Result.success(filesService.upload(file)); //返回结果
    }



    @PostMapping("/editor/upload")
    public JSON editorUpload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename(); //获取源文件的名称
        //定义文件的唯一标识
        String flag = IdUtil.fastSimpleUUID();
        String rootFilePath = System.getProperty("user.dir") + "/springboot/src/main/resources/files/" + flag + "_" + originalFilename; //获取上传路径
        FileUtil.writeBytes(file.getBytes(), rootFilePath); //把文件写入上传的路径
        String url = serverIp + ":" + port + "/files/" + flag;
        JSONObject json = new JSONObject();
        json.set("errno", 0);
        JSONArray arr = new JSONArray();
        JSONObject data = new JSONObject();
        arr.add(data);
        data.set("url", url);
        json.set("data", arr);
        return json; //返回结果
    }

    @GetMapping("/{flag}")
    public void getFiles(@PathVariable String flag, HttpServletResponse response) throws IOException {
        OutputStream os;
        String basePath = System.getProperty("user.dir") + "/springboot/src/main/resources/files/"; //定义文件上传的更路径
        List<String> fileNames = FileUtil.listFileNames(basePath); //获取所有的文件名称
        String fileName = fileNames.stream().filter(name -> name.contains(flag)).findAny().orElse(""); //找到跟参数一致的文件
        try {
            if (StrUtil.isNotEmpty(fileName)) {
                response.addHeader("Content-Disposition", "attachment;fiename=" + URLEncoder.encode(fileName, "UTF-8"));
                byte[] bytes = FileUtil.readBytes(basePath + fileName); //
                os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
            }
        } catch (Exception e) {
            System.out.println("文件下载失败");
        }
    }

    @GetMapping("/detail/{id}")
    public Result getById(@PathVariable Integer id){
        return Result.success(filesService.getById(id));
    }
//    @GetMapping
//    public Result getAllFiles(@PathVariable Integer id){
//        return Result.success(filesMapper.selectById(id));
//    }
    @GetMapping
    public Result getPage(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "") String search) {
        return Result.success(filesService.getPage(pageNum, pageSize, search));
    }
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(filesService.delete(id));
    }
    @PostMapping("/batch/delete")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(filesService.deleteBatch(ids));
    }
}
