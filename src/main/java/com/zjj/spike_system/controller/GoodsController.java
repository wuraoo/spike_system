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
    public Result showGoods(Integer pageNum, Integer pageSize){
        Result res = goodsService.getGoods(pageNum,pageSize);
        return res;
    }

}

