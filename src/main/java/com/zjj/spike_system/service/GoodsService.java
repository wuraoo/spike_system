package com.zjj.spike_system.service;

import com.zjj.spike_system.entity.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjj.spike_system.utils.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zjj
 * @since 2021-11-15
 */
public interface GoodsService extends IService<Goods> {

    Result getGoods(Integer pageNum, Integer pageSize);
}
