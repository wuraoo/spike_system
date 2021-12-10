package com.zjj.spike_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zjj.spike_system.entity.Order;
import com.zjj.spike_system.entity.Skgoods;
import com.zjj.spike_system.entity.Skorder;
import com.zjj.spike_system.entity.User;
import com.zjj.spike_system.mapper.OrderMapper;
import com.zjj.spike_system.mapper.SkorderMapper;
import com.zjj.spike_system.service.SkgoodsService;
import com.zjj.spike_system.service.SkorderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjj.spike_system.utils.MD5Util;
import com.zjj.spike_system.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
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
    private SkorderMapper skorderMapper;
    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public Result addOrder(User user, Skgoods skgoods) {
        // 更新库存
        skgoods.setSpikeStock(skgoods.getSpikeStock()-1);
        skgoodsService.update(new UpdateWrapper<Skgoods>().setSql("spike_stock = spike_stock - 1").eq("id", skgoods.getId()).gt("spike_stock", 0));
        // 库存不足,如果redis中有该id的key，则表示该商品已经秒杀完
        if (skgoods.getSpikeStock() < 1){
            redisTemplate.opsForValue().set("isStockEmpty:" + skgoods.getId(), 0);
            return null;
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

    /**
     * 获取用户秒杀结果，确认是否成功
     * @param user
     * @param goodId
     * @return  >0表示秒杀成功   -1表示失败   0表示排队
     */
    @Override
    public Long confirmSkResult(User user, Long goodId) {
        QueryWrapper<Skorder> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", user.getId()).eq("goods_id", goodId);
        Skorder skorder = skorderMapper.selectOne(wrapper);
        // 已经查询到
        if (skorder != null){
            return skorder.getId();
        }
        // 如果库存为0则表示失败
        else if (redisTemplate.hasKey("isStockEmpty:" + goodId)){
            return -1L;
        }
        else {
            return 0L;
        }
    }

    /**
     * 获取隐藏地址
     * @param goodId
     * @param userId
     * @return
     */
    @Override
    public String getPath(Long goodId, Long userId) {
        // 获取随机值并加密，作为隐藏路径
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String path = MD5Util.md5(uuid);
        redisTemplate.opsForValue().set("skPath:" + goodId +  ":" + userId, path, 30, TimeUnit.SECONDS);
        return path;
    }

    /**
     * 验证隐藏地址是否正确
     * @param path
     * @param goodId
     * @param userId
     * @return
     */
    @Override
    public boolean checkPath(String path, Long goodId, Long userId) {
        String redisPath = (String) redisTemplate.opsForValue().get("skPath:" + goodId + ":" + userId);
        if (path == null || userId <= 0 || goodId <= 0 || redisPath == null){
            return false;
        }
        if (path.equals(redisPath))
            return true;
        return false;
    }
}
