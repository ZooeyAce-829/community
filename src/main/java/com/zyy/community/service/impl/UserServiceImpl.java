package com.zyy.community.service.impl;

import com.zyy.community.dao.UserDao;
import com.zyy.community.entity.User;
import com.zyy.community.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public User judgeByAccountId(User user) {
        // u: 数据库查询出来的user
        User u = userDao.selectUserById(user.getAccount_id());

        if (u == null) {
            // 数据库中没有此account_id的用户
            user.setGmt_create(System.currentTimeMillis());
            user.setGmt_modify(user.getGmt_create());
            int count = userDao.insertUser(user);
            if (count == 1) log.info("插入成功");
        } else {
            u.setGmt_modify(System.currentTimeMillis());
            u.setAvatar_url(u.getAvatar_url());
            u.setName(user.getName());
            u.setToken(user.getToken());
            userDao.update(u);
        }

        return user;
    }
}
