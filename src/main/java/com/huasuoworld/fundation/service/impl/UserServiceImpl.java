package com.huasuoworld.fundation.service.impl;

import com.google.inject.Inject;
import com.huasuoworld.fundation.bo.User;
import com.huasuoworld.fundation.mapper.UserMapper;
import com.huasuoworld.fundation.service.UserService;

public class UserServiceImpl implements UserService {

    private UserMapper userMapper;

    @Inject
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User selectById(String id) {
        return userMapper.selectById(id);
    }
}
