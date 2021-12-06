package com.zjj.spike_system.entity.message;

import com.zjj.spike_system.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品秒杀的消息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkMessage {
    private User user;
    private Long goodId;
}
