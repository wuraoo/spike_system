package com.zjj.spike_system.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * rabbitmq的配置类
 */
@Configuration
public class RabbitMQTopicConfig {

    // 主题交换机名称
    private static final String SK_EXCHANGE = "sk_exchange";
    // 队列名称
    private static final String SK_QUEUE = "sk_queue";
    // 商品秒杀路由键名称
    private static final String SK_ROUTING_KEY  = "sk.order";

    /**
     * 队列声明
     * @return
     */
    @Bean
    public Queue skQueue(){
        return new Queue(SK_QUEUE);
    }

    /**
     * 交换机声明
     * @return
     */
    @Bean
    public TopicExchange skExchange(){
        return new TopicExchange(SK_EXCHANGE);
    }

    /**
     * 队列和交换机的绑定
     * @return
     */
    @Bean
    public Binding skQueueBinding(){
        return BindingBuilder.bind(skQueue()).to(skExchange()).with(SK_ROUTING_KEY);
    }
}
