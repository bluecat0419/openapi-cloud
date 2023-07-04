package com.zty.common.core.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * 签名工具
 * @author ZhangTianYou
 */
public class SignUtil {

    public static String sign(String secretKey){
        return new Digester(DigestAlgorithm.SHA256).digestHex(secretKey);
    }

}
