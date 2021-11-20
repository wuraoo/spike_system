package com.zjj.spike_system.mapper;

import com.zjj.spike_system.entity.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zjj
 * @since 2021-11-15
 */
@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {

}
