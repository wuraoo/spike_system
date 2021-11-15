package com.zjj.spike_system.service.impl;

import com.zjj.spike_system.entity.Order;
import com.zjj.spike_system.mapper.OrderMapper;
import com.zjj.spike_system.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
