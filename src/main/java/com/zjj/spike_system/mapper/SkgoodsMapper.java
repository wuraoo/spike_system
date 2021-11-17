package com.zjj.spike_system.mapper;

import com.zjj.spike_system.entity.Skgoods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjj.spike_system.entity.vo.SkGoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zjj
 * @since 2021-11-15
 */
public interface SkgoodsMapper extends BaseMapper<Skgoods> {

    List<SkGoodsVo> getAllSkGoods();
}
