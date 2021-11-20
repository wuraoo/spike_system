package com.zjj.spike_system.service.impl;

import com.zjj.spike_system.entity.Goods;
import com.zjj.spike_system.entity.Order;
import com.zjj.spike_system.entity.User;
import com.zjj.spike_system.mapper.GoodsMapper;
import com.zjj.spike_system.mapper.OrderMapper;
import com.zjj.spike_system.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjj.spike_system.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zjj
 * @since 2021-11-15
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public Result addOrder(User user, Long id, String goodName, BigDecimal goodPrice, int stock) {
        //创建订单，并默认支付
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(id);
        order.setGoodsName(goodName);
        order.setGoodsCount(1);
        order.setGoodsPrice(goodPrice);
        order.setGoodsState(1);
        order.setGmtPay(new Date());
        boolean save = this.save(order);
        // 修改商品详细（库存）
        Goods goods = new Goods();
        goods.setId(id);
        goods.setGoodsStock(stock-1);
        goodsMapper.updateById(goods);
        // 购买成功
        if (save){
            return Result.ok();
        }
        return Result.error();
    }
}
