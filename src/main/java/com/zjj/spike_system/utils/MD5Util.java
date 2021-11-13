package com.zjj.spike_system.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component
public class MD5Util {
    // md5加密方法
    public static String md5(String pwd){
        return DigestUtils.md5Hex(pwd);
    }

    // 模拟整个流程，结合前两者
    public static String inputPassToDBPass(String inputPass, String slat){
        String str = slat.charAt(5) + slat.charAt(2) + inputPass + slat.charAt(3) + slat.charAt(1);
        String dbPass = md5(str);
        return dbPass;
    }

}
