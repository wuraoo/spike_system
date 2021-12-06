package com.zjj.spike_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zjj.spike_system.entity.Order;
import com.zjj.spike_system.entity.Skgoods;
import com.zjj.spike_system.entity.Skorder;
import com.zjj.spike_system.entity.User;
import com.zjj.spike_system.mapper.OrderMapper;
import com.zjj.spike_system.mapper.SkgoodsMapper;
import com.zjj.spike_system.mapper.SkorderMapper;
import com.zjj.spike_system.service.SkgoodsService;
import com.zjj.spike_system.service.SkorderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjj.spike_system.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zjj
 * @since 2021-11-15
 */
@Service
@Transactional
@Slf4j
public class SkorderServiceImpl extends ServiceImpl<SkorderMapper, Skorder> implements SkorderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private SkgoodsService skgoodsService;

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public Result addOrder(User user, Skgoods skgoods) {
        // 更新库存,这里存在问题
        boolean isUpdate = skgoodsService.update(new UpdateWrapper<Skgoods>().setSql("spike_stock = spike_stock - 1").eq("id", skgoods.getId()).gt("spike_stock", 0));
        if (isUpdate == false){
            return Result.error().setMessage("来晚了~已经抢完了~");
        }
        //创建普通订单，并默认支付
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(skgoods.getGoodsId());
        order.setGoodsName("秒杀商品");
        order.setGoodsCount(1);
        order.setGoodsPrice(skgoods.getSpikePrice());
        orderMapper.insert(order);

        // 创建秒杀订单
        Skorder skorder = new Skorder();
        skorder.setUserId(user.getId());
        skorder.setGoodsId(skgoods.getId());
        skorder.setOrderId(order.getId());
        boolean save = this.save(skorder);

        // 成功则秒杀成功
        if (save){
            // 将秒杀订单放在Redis中，当用户重复秒杀的时候只需要从Redis中获取判断即可
            redisTemplate.opsForValue().set("skorder" + user.getId() + skgoods.getId(), skorder,1, TimeUnit.MINUTES);
            return Result.ok();
        }

        return null;
    }
}
