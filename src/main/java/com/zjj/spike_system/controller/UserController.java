package com.zjj.spike_system.controller;


import com.zjj.spike_system.entity.User;
import com.zjj.spike_system.utils.Result;
import com.zjj.spike_system.utils.ResultCode;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
public class UserController {


    @ApiOperation("用户登录方法")
    @PostMapping("login")
    public Result userLogin(User user){

        return Result.setResult(ResultCode.SUCCESS);
    }

}

