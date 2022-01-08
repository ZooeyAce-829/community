package com.zyy.community.dao;

import com.zyy.community.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {

    int insertUser(User user);
}
