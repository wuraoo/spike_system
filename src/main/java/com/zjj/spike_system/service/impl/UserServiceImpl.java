package com.zjj.spike_system.service.impl;

import com.zjj.spike_system.entity.User;
import com.zjj.spike_system.mapper.UserMapper;
import com.zjj.spike_system.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zjj
 * @since 2021-11-12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
