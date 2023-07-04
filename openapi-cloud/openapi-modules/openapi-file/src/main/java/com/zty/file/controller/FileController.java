package com.zty.file.controller;

import com.zty.file.config.AliyunConfig;
import com.zty.file.config.StorageConfig;
import com.zty.file.factory.StorageFactory;
import com.zty.file.service.FileService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传控制器
 * @author ZhangTianYou
 */
@ApiModel("文件")
@RestController
public class FileController {

    @PostMapping("upload")
    @ApiOperation(value = "文件上传")
    public String upload(MultipartFile file){
        FileService storage = StorageFactory.getStorage();
        return storage.upload(file);
    }

}
