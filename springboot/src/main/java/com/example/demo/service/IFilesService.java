package com.example.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.common.Result;
import com.example.demo.entity.Files;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IFilesService extends IService<Files> {
    IPage<Files> getPage(Integer pageNum, Integer pageSize, String search);
    Boolean delete(Integer id);
    Boolean deleteBatch(List<Integer> ids);
    String upload(MultipartFile file) throws IOException;
}
