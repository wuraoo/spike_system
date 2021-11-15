package com.zjj.spike_system.config;

import com.zjj.spike_system.entity.User;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 该类用于WebConfig配置类的addArgumentResolvers方法传入
 */
@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    /**
     * 该方法用于判断Controller中方法参数中是否有符合条件的参数：
     *                          有则进入下一个方法resolveArgument；
     *                          没有则跳过不做处理
     * @param parameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 获取传入参数的类型
        Class<?> type = parameter.getParameterType();
        // 如果参数类型为User则符合,进入resolveArgument方法
        if (User.class == type)
            return true;
        return false;
    }

    /**
     * 该方法在上一个方法同通过之后调用：
     *      在这里可以进行处理，根据情况返回对象——返回的对象将被赋值到Controller的方法的参数中
     * @param parameter
     * @param mavContainer
     * @param webRequest
     * @param binderFactory
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpSession session = request.getSession();
        Cookie[] cookies = request.getCookies();
        String token = null;
        for (Cookie c : cookies){
            if (c.getName().equals("token")){
                token = c.getValue();
                break;
            }
        }
        System.out.println("==============" + token);
        // 如果token不存在，则返回null
        if (token == null) return null;
        // 获取session中对象
        User user = (User)session.getAttribute(token);
        return user;
    }
}
