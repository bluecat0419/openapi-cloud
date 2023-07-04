package com.zty.file.factory;

import cn.hutool.extra.spring.SpringUtil;
import com.zty.file.config.StorageConfig;
import com.zty.file.service.FileService;
import com.zty.file.service.impl.AliyunFileServiceImpl;
import com.zty.file.service.impl.LocalFileServiceImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * 存储工厂类
 * @author ZhangTianYou
 */
public class StorageFactory {

    private static Map<String, FileService> STORAGE_MAP=new HashMap<>(5);

    private static StorageConfig storageConfig = SpringUtil.getBean(StorageConfig.class);

    static {
        STORAGE_MAP.put(StorageType.LOCAL,new LocalFileServiceImpl());
        STORAGE_MAP.put(StorageType.ALIYUN,new AliyunFileServiceImpl());
    }

    public static FileService getStorage() {
        return STORAGE_MAP.get(storageConfig.getType()) == null ?
                STORAGE_MAP.get(StorageType.LOCAL) : STORAGE_MAP.get(storageConfig.getType());
    }

    public interface StorageType {
        String LOCAL = "local";

        String ALIYUN = "aliyun";
    }

}
