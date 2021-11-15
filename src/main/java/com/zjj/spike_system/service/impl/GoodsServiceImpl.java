package com.zjj.spike_system.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjj.spike_system.entity.Goods;
import com.zjj.spike_system.mapper.GoodsMapper;
import com.zjj.spike_system.service.GoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjj.spike_system.utils.Result;
import com.zjj.spike_system.utils.ResultCode;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zjj
 * @since 2021-11-15
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Override
    public Result getGoods(Integer pageNum, Integer pageSize) {
        Page<Goods> page = new Page<>(pageNum,pageSize);
        this.page(page, null);
        List<Goods> goods = page.getRecords();
        // 查询到则数据返回数据
        if (goods.size() > 0){
            return Result.ok().setData("goods", page);
        }else{
            return Result.error().setMessage("查询失败");
        }
    }
}
