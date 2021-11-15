package com.zjj.spike_system.controller;


import com.sun.deploy.net.HttpResponse;
import com.zjj.spike_system.entity.User;
import com.zjj.spike_system.entity.vo.LoginVo;
import com.zjj.spike_system.service.UserService;
import com.zjj.spike_system.utils.Result;
import com.zjj.spike_system.utils.ResultCode;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zjj
 * @since 2021-11-12
 */
@RestController
@RequestMapping("/spike_system/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation("显示商品信息访问接口")
    @GetMapping("goods")
    public Result showGoods(User user){
        // 注意：这里的User参数不是由前端传入的，而是由addArgumentResolvers方法处理之后传进来的
        log.info(user.toString());
        if (user == null)
            return Result.error();
        return Result.ok();
    }


    @ApiOperation("用户登录接口")
    @PostMapping("login")
    public Result userLogin(@RequestBody LoginVo user, HttpServletResponse response,HttpSession session){
        log.info(user.toString());
        // 从数据库获取用户信息并判断是否正确
        Result result = userService.userLogin(user);
        if (result.getData() != null){
            String token = UUID.randomUUID().toString().replace("-", "");
            // session设置值
            session.setAttribute(token, result.getData().get("user"));
            // 创建cookie
            Cookie cookie = new Cookie("token", token);
            // 添加cookie
            response.addCookie(cookie);
        }
        return  result;
    }

}

