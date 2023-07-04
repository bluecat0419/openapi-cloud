package com.zty.file.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.zty.common.core.exception.BusinessException;
import com.zty.file.config.LocalConfig;
import com.zty.file.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

/**
 * 本地文件上传
 * @author ZhangTianYou
 */
@Slf4j
@Service
public class LocalFileServiceImpl implements FileService {

    LocalConfig localConfig = SpringUtil.getBean(LocalConfig.class);

    @Override
    public String upload(MultipartFile file) {
        String filePath = StringUtils.endsWith(localConfig.getLocalFilePath(),"/") ?
                localConfig.getLocalFilePath() : localConfig.getLocalFilePath() + "/";
        File localFile = new File(filePath + file.getOriginalFilename());

        try {
            FileUtils.copyToFile(new ByteArrayInputStream(file.getBytes()),localFile);
        } catch (IOException e) {
            log.error("文件上传失败",e);
            throw new BusinessException("上传失败");
        }
        return localConfig.getDomain() + "/" + localFile.getPath();
    }

}
