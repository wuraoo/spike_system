package com.zjj.spike_system.rabbitmq;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjj.spike_system.entity.Skgoods;
import com.zjj.spike_system.entity.Skorder;
import com.zjj.spike_system.entity.User;
import com.zjj.spike_system.entity.message.SkMessage;
import com.zjj.spike_system.service.SkgoodsService;
import com.zjj.spike_system.service.SkorderService;
import com.zjj.spike_system.utils.JsonUtil;
import com.zjj.spike_system.utils.Result;
import jdk.nashorn.internal.ir.CallNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * MQ消费类
 */
@Service
@Slf4j
public class MQReceiver {

    @Autowired
    private SkgoodsService skgoodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SkorderService skorderService;

    /**
     * 接收队列中的消息并处理
     * @param message
     */
    @RabbitListener(queues = "sk_queue")
    public void receiver(Message message){
        log.info("接收到消息：" + new String(message.getBody()));
        // 将消息转化为对象
        SkMessage skMessage = JsonUtil.string2Obj(new String(message.getBody()), SkMessage.class);
        // 下单操作
        User user = skMessage.getUser();
        Long goodId = skMessage.getGoodId();
        Skgoods skGood = skgoodsService.getById(goodId);
        // 判断库存
        if (skGood.getSpikeStock() < 1){
            return;
        }
        // 判断是否重复抢购
        QueryWrapper<Skorder> wrapper = new QueryWrapper<>();
        wrapper.eq("goods_id", goodId).eq("user_id", user.getId());
        Skorder isSpiked = skorderService.getOne(wrapper);
        if (!Objects.isNull(isSpiked)){
            return;
        }
        // 下单
        skorderService.addOrder(user, skGood);
    }

}
