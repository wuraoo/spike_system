package com.zjj.spike_system.service;

import com.zjj.spike_system.entity.Skgoods;
import com.zjj.spike_system.entity.Skorder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjj.spike_system.entity.User;
import com.zjj.spike_system.utils.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zjj
 * @since 2021-11-15
 */
public interface SkorderService extends IService<Skorder> {

    Result addOrder(User user, Skgoods skgoods);

    Long confirmSkResult(User user, Long goodId);
}
