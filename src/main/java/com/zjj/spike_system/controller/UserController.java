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

    @GetMapping("goods")
    public Result showGoods(HttpSession session, @CookieValue("token") String token){
        log.info("token:" + token);
        log.info(session.getAttribute(token).toString());
        if (token == null)
            return Result.error();
        if (session.getAttribute(token) == null)
            return Result.error();
        return Result.ok();
    }


    @ApiOperation("用户登录方法")
    @PostMapping("login")
    public Result userLogin(@RequestBody LoginVo user, HttpServletResponse response,HttpSession session){
        log.info(user.toString());
        Result result = userService.userLogin(user);
        if (result.getData() != null){
            System.out.println(result.getData());
            String token = UUID.randomUUID().toString().replace("-", "");
            session.setAttribute(token, result.getData());
            Cookie cookie = new Cookie("token", token);
            response.addCookie(cookie);
        }
        return  result;
    }

}

