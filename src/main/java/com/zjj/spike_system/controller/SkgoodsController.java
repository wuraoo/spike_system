package com.zjj.spike_system.controller;


import com.zjj.spike_system.entity.User;
import com.zjj.spike_system.entity.vo.SkGoodsVo;
import com.zjj.spike_system.service.SkgoodsService;
import com.zjj.spike_system.utils.Result;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zjj
 * @since 2021-11-15
 */
@RestController
@RequestMapping("/spike_system/skgoods")
@Slf4j
public class SkgoodsController {

    @Autowired
    private SkgoodsService skgoodsService;

    @Autowired
    private RedisTemplate redisTemplate;


    @ApiOperation("秒杀商品信息访问接口")
    @GetMapping("info")
    public Result showGoods(User user){
        // 注意：这里的User参数不是由前端传入的，而是由addArgumentResolvers方法处理之后传进来的
        if (user == null)
            return Result.error().setMessage("请先登录");

        // 先从Redis中查询数据，有则直接返回
        List<SkGoodsVo> allSkGoodsList = (List<SkGoodsVo>) redisTemplate.opsForValue().get("skgoods");
        if (allSkGoodsList != null && allSkGoodsList.size() > 0){
            return Result.ok().setData("skgoods",allSkGoodsList);
        }

        // 否则查询数据库，并将结果放到Redis中缓存
        // 查询秒杀商品（封装到SkGoodsVo中返回）
        Result res = skgoodsService.getGoods();
        return res;
    }

}

