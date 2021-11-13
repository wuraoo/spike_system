package com.zjj.spike_system.service;

import com.zjj.spike_system.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjj.spike_system.entity.vo.LoginVo;
import com.zjj.spike_system.utils.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zjj
 * @since 2021-11-12
 */
public interface UserService extends IService<User> {

    Result userLogin(LoginVo user);
}
