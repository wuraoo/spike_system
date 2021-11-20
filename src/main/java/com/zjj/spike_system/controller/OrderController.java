package com.zjj.spike_system.controller;


import com.baomidou.mybatisplus.extension.api.R;
import com.zjj.spike_system.entity.Goods;
import com.zjj.spike_system.entity.User;
import com.zjj.spike_system.service.GoodsService;
import com.zjj.spike_system.service.OrderService;
import com.zjj.spike_system.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zjj
 * @since 2021-11-15
 */
@RestController
@RequestMapping("/spike_system/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private GoodsService goodsService;

    // 一般商品的购买（处于方便，前端点击购买按钮之后直接跳转到订单页面并完成支付）
    @GetMapping("buy/{id}")
    public Result buyNow(User user,@PathVariable("id") Long id){
        // 未登录，返回错误码提示用户登录
        if (user == null){
            return Result.error().setCode(22222).setMessage("请先登录");
        }

        // 获取商品信息，判断库存是否足够
        Goods goods = goodsService.getById(id);
        Integer stock = goods.getGoodsStock();
        String goodsName = goods.getGoodsName();
        BigDecimal goodsPrice = goods.getGoodsPrice();
        if (stock <= 0){
            return Result.error().setMessage("对不起,库存不足");
        }

        // 下订单，并完成购买
        Result res = orderService.addOrder(user,id,goodsName,goodsPrice,stock);
        return res;
    }

}

