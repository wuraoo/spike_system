package com.zjj.spike_system.service;

import com.zjj.spike_system.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjj.spike_system.entity.User;
import com.zjj.spike_system.utils.Result;

import java.math.BigDecimal;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zjj
 * @since 2021-11-15
 */
public interface OrderService extends IService<Order> {

    Result addOrder(User user, Long id, String name, BigDecimal price,int stock);
}
