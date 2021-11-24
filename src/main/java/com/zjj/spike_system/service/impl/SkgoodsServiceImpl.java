package com.zjj.spike_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjj.spike_system.entity.Skgoods;
import com.zjj.spike_system.entity.vo.SkGoodsVo;
import com.zjj.spike_system.mapper.SkgoodsMapper;
import com.zjj.spike_system.service.SkgoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjj.spike_system.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zjj
 * @since 2021-11-15
 */
@Service
public class SkgoodsServiceImpl extends ServiceImpl<SkgoodsMapper, Skgoods> implements SkgoodsService {

    @Autowired
    private SkgoodsMapper skgoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Result getGoods() {
        List<SkGoodsVo> skGoods = skgoodsMapper.getAllSkGoods();
        redisTemplate.opsForValue().set("skgoods", skGoods);
        return Result.ok().setData("skgoods", skGoods);
    }
}
