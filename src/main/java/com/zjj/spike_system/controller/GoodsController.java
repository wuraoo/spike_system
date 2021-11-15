package com.zjj.spike_system.controller;


import com.zjj.spike_system.entity.User;
import com.zjj.spike_system.service.GoodsService;
import com.zjj.spike_system.utils.Result;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zjj
 * @since 2021-11-15
 */
@RestController
@RequestMapping("/spike_system/goods")
@Slf4j
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @ApiOperation("显示商品信息访问接口")
    @GetMapping("info")
    public Result showGoods(User user, Integer pageNum, Integer pageSize){
        // 注意：这里的User参数不是由前端传入的，而是由addArgumentResolvers方法处理之后传进来的
        log.info(user.toString());
        if (user == null)
            return Result.error();
        // 查询一般商品
        Result res = goodsService.getGoods(pageNum,pageSize);
        return res;
    }

}

