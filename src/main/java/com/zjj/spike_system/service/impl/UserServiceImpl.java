package com.zjj.spike_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjj.spike_system.entity.User;
import com.zjj.spike_system.entity.vo.LoginVo;
import com.zjj.spike_system.mapper.UserMapper;
import com.zjj.spike_system.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjj.spike_system.utils.MD5Util;
import com.zjj.spike_system.utils.Result;
import com.zjj.spike_system.utils.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

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

    @Override
    public Result userLogin(LoginVo user) {
        if (user.getNickname() == null){
            return Result.setResult(ResultCode.LOGIN_ERROR);
        }
        // 1.根据用户名查询用户信息
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("nickname", user.getNickname());
        User one = this.getOne(wrapper);
        if (one != null){
            // 2.获取盐值并加密
            String slat = one.getSlat();
            String md5Pass = MD5Util.inputPassToDBPass(user.getPassword(), slat);
            // 3.密码比较
            if (StringUtils.equals(md5Pass, one.getPassword())){
                // 密码一致
                // 登陆次数+1，并自动填充最后修改时间
                one.setLogincount(one.getLogincount() + 1);
                this.updateById(one);
                return Result.ok().setData("user", one);
            }
        }
        return Result.setResult(ResultCode.LOGIN_ERROR);
    }

}
