package com.example.demo.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Files;
import com.example.demo.mapper.FilesMapper;
import com.example.demo.service.IFilesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class FilesServiceImpl extends ServiceImpl<FilesMapper, Files> implements IFilesService {
    @Value("${server.port}")
    private String port;

    @Value("${server.ip}")
    private String serverIp;

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Resource
    FilesMapper filesMapper;

    @Override
    public IPage<Files> getPage(Integer pageNum, Integer pageSize, String search) {
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete", false);
        queryWrapper.orderByDesc("id");
        if (!"".equals(search)) {
            queryWrapper.like("name", search);
        }
        return filesMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
    }

    @Override
    public Boolean delete(Integer id) {
        Files files = filesMapper.selectById(id);
        files.setIsDelete(true);
        filesMapper.updateById(files);
        return true;
    }

    @Override
    public Boolean deleteBatch(List<Integer> ids) {
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        List<Files> files = filesMapper.selectList(queryWrapper);
        for (Files file : files) {
            file.setIsDelete(true);
            filesMapper.updateById(file);
        }
        return true;
    }

    @Override
    public String upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename(); //获取源文件的名称
        String type = FileUtil.extName(originalFilename); //获取文件类型
        long size = file.getSize();
        //定义文件的唯一标识
        String flag = IdUtil.fastSimpleUUID() + StrUtil.DOT + type;
        File uploadFile = new File(fileUploadPath + flag);
        //判断目录是否存在
        File parentFile = uploadFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        String url;
        // 获取文件的md5
        String md5 = SecureUtil.md5(file.getInputStream());
        // 从数据库查询是否存在相同的记录
        Files dbFiles = getFileByMd5(md5);
        if (dbFiles != null) {
            url = dbFiles.getUrl();
        } else {
            // 上传文件到磁盘
            file.transferTo(uploadFile);
            // 数据库若不存在重复文件，则不删除刚才上传的文件
            url = "http://" + serverIp + ":9090/files/" + flag;
        }
        // 存储数据库
        Files saveFile = new Files();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size/1024); //单位 kb
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        saveFile.setIsDelete(false);
        saveFile.setEnable(true);
        saveFile.setFlagName(flag);
        filesMapper.insert(saveFile);

        return url;
    }

    private Files getFileByMd5(String md5) {
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        List<Files> filesList = filesMapper.selectList(queryWrapper);
        return filesList.size() == 0 ? null : filesList.get(0);
    }
}
