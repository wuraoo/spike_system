package com.zjj.spike_system.service;

import com.zjj.spike_system.entity.Skgoods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjj.spike_system.entity.vo.SkGoodsVo;
import com.zjj.spike_system.utils.Result;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zjj
 * @since 2021-11-15
 */
public interface SkgoodsService extends IService<Skgoods> {

    List<SkGoodsVo> getGoods();
}
