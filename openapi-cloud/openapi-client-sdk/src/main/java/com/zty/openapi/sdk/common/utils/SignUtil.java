package com.zty.openapi.sdk.common.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 *
 * @author ZhangTianYou
 */
public class SignUtil {

    public static String sign(String secretKey){
        return new Digester(DigestAlgorithm.SHA256).digestHex(secretKey);
    }

}
