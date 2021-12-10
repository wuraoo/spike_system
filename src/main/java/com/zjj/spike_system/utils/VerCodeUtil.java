package com.zjj.spike_system.utils;

import com.wf.captcha.*;
import com.wf.captcha.base.Captcha;

import java.util.HashMap;
import java.util.Map;

/**
 * 验证码生成工具类
 */
public class VerCodeUtil {

    /**
     * 算数验证码
     * @param width
     * @param height
     * @param len 表示验证码的位数
     * @return
     */
    public static Map<String, String> getArithmeticCaptcha  (int width, int height, int len){
        ArithmeticCaptcha arithmeticCaptcha = new ArithmeticCaptcha(width, height);
        arithmeticCaptcha.setLen(len);
        return getCaptchaMap(arithmeticCaptcha);
    }

    /**
     * 获取中文验证码
     * @param width
     * @param height
     * @param len
     * @return
     */
    public static Map<String, String> getChineseCaptcha (int width, int height, int len){
        ChineseCaptcha  specCaptcha = new ChineseCaptcha(width, height, len);
        return getCaptchaMap(specCaptcha);
    }

    /**
     * 获取中文gif验证码
     * @param width
     * @param height
     * @param len
     * @return
     */
    public static Map<String, String> getChineseGifCaptcha  (int width, int height, int len){
        ChineseGifCaptcha   specCaptcha = new ChineseGifCaptcha(width, height, len);
        return getCaptchaMap(specCaptcha);
    }

    /**
     * 获取png类型验证码
     * @param width
     * @param height
     * @param len
     * @return
     */
    public static Map<String, String> getSpecCaptcha(int width, int height, int len){
        SpecCaptcha specCaptcha = new SpecCaptcha(width, height, len);
        return getCaptchaMap(specCaptcha);
    }

    /**
     * 获取GIF类型验证码
     * @param width
     * @param height
     * @param len
     * @return
     */
    public static Map<String, String> getSpecGifCaptcha(int width, int height, int len){
        GifCaptcha gifCaptcha = new GifCaptcha(130,48,4);
        return getCaptchaMap(gifCaptcha);
    }


    private static Map<String, String> getCaptchaMap(Captcha captcha){
        Map<String, String> specCaptchaMap = new HashMap<>();
        // 验证码(值)
        String verCode = captcha.text().toLowerCase();
        specCaptchaMap.put("verCode",verCode);
        // base64图像
        String image = captcha.toBase64();
        specCaptchaMap.put("image",image);
        return specCaptchaMap;
    }
}
