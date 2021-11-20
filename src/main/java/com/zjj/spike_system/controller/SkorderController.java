package com.zjj.spike_system.controller;


import com.zjj.spike_system.entity.Skgoods;
import com.zjj.spike_system.entity.User;
import com.zjj.spike_system.mapper.SkgoodsMapper;
import com.zjj.spike_system.mapper.SkorderMapper;
import com.zjj.spike_system.service.SkgoodsService;
import com.zjj.spike_system.service.SkorderService;
import com.zjj.spike_system.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/spike_system/skorder")
public class SkorderController {


    @Autowired
    private SkorderService skorderService;

    @Autowired
    private SkgoodsService  skgoodsService;

    @GetMapping("buy/{goodId}")
    public Result SpikeGoods(User user, @PathVariable("goodId") Long goodId){
        // 用户未登录（一般不会出现，因为未登录不出显示秒杀页面）
        if (user == null){
            return Result.error().setCode(22222).setMessage("请先登录");
        }
        // 判断库存
        Skgoods skgoods = skgoodsService.getById(goodId);
        Integer spikeStock = skgoods.getSpikeStock();
        if (spikeStock <= 0){
            return Result.error().setMessage("对不起,库存不足");
        }

        // 创订单，并购买
        Result res = skorderService.addOrder(user, skgoods);

        return res;
    }




}

