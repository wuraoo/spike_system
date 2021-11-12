package com.zjj.spike_system.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component
public class MD5Util {

    private static final String slat  = "123abc";

    // md5加密方法
    public static String md5(String pwd){
        return DigestUtils.md5Hex(pwd);
    }

    // 模拟前端加密
    public static String inputPassToFromPass(String inputPass){
        String str = slat.charAt(2) + slat.charAt(0) + inputPass + slat.charAt(4) + slat.charAt(1);
        return md5(str);
    }

    // 后端加密(将前端传过来的密码加密)
    public static String fromPassToDBPass(String fromPass, String slat){
        String str = slat.charAt(5) + slat.charAt(2) + fromPass + slat.charAt(3) + slat.charAt(1);
        return md5(str);
    }

    // 模拟整个流程，结合前两者
    public static String inputPassToDBPass(String inputPass, String slat){
        String fromPass = inputPassToFromPass(inputPass);
        String dbPass = fromPassToDBPass(fromPass, slat);
        return dbPass;
    }

}
