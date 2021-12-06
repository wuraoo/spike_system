package com.zjj.spike_system.rabbitmq;

import com.zjj.spike_system.config.RabbitMQTopicConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * mq消息发送类
 */
@Service
@Slf4j
public class MQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送秒杀消息
     * @param message
     */
    public void sendSkOrderMessage(String message){
        log.info("发送消息：" + message);
        rabbitTemplate.convertAndSend("sk_exchange", "sk.order", message);
    }


}
