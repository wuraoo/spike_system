package com.zjj.spike_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjj.spike_system.entity.Order;
import com.zjj.spike_system.entity.Skgoods;
import com.zjj.spike_system.entity.Skorder;
import com.zjj.spike_system.entity.User;
import com.zjj.spike_system.mapper.SkgoodsMapper;
import com.zjj.spike_system.mapper.SkorderMapper;
import com.zjj.spike_system.service.SkorderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjj.spike_system.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
public class SkorderServiceImpl extends ServiceImpl<SkorderMapper, Skorder> implements SkorderService {

    @Autowired
    private SkgoodsMapper skgoodsMapper;


    @Override
    public Result addOrder(User user, Skgoods skgoods) {
        // 判断当前用户是否已经秒杀过
        // 查询当前用户是否有该商品的秒杀记录
        Skorder isHasOrder = this.getOne(new QueryWrapper<Skorder>().eq("user_id", user.getId()).eq("goods_id", skgoods.getId()));
        if (isHasOrder != null){
            return Result.error().setMessage("您已经抢过了噢~下次再来");
        }

        //创建普通订单，并默认支付(此步省略)

        // 创建秒杀订单
        Skorder skorder = new Skorder();
        skorder.setUserId(user.getId());
        skorder.setGoodsId(skgoods.getId());
        boolean save = this.save(skorder);

        skgoods.setSpikeStock(skgoods.getSpikeStock() - 1);
        skgoodsMapper.updateById(skgoods);

        // 成功则秒杀成功
        if (save){
            return Result.ok();
        }

        return null;
    }
}
