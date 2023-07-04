package com.zty.file.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.zty.common.core.exception.BusinessException;
import com.zty.file.config.AliyunConfig;
import com.zty.file.service.FileService;
import com.zty.file.utils.AliyunUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 阿里云文件上传
 * @author ZhangTianYou
 */
@Slf4j
@Service
public class AliyunFileServiceImpl implements FileService {

    AliyunConfig aliyunConfig = SpringUtil.getBean(AliyunConfig.class);

    @Override
    public String upload(MultipartFile file) {
        OSSClient client = new OSSClient(aliyunConfig.getEndPoint(), aliyunConfig.getAccessKeyId(), aliyunConfig.getAccessKeySecret());
        //文件名称
        String fileName = file.getOriginalFilename();
        //文件后缀
        String extension= "."+ FilenameUtils.getExtension(file.getOriginalFilename());
        //上传路径
        String path = aliyunConfig.getPrefix()+fileName;
        try {
            ObjectMetadata objectMetadata=new ObjectMetadata();
            objectMetadata.setContentLength(file.getInputStream().available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("pragma","no-cache");
            objectMetadata.setContentType(AliyunUtils.getContentType(extension));
            objectMetadata.setContentDisposition("inline;filename="+fileName);

            //上传文件
            client.putObject(aliyunConfig.getBucketName(),path, file.getInputStream(),objectMetadata);
            client.shutdown();
        } catch (Exception e){
            log.error("文件上传失败,原因:{},文件名:{},上传路径:{}",e.getMessage(),fileName,path);
            throw new BusinessException("上传失败");
        }

        String cloudPath=aliyunConfig.getDomain() + "/" + path;
        log.info("文件上传成功,文件路径:{}",cloudPath);
        return cloudPath;
    }

}
