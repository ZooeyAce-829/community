package com.zyy.community.dao;

import com.zyy.community.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserDao {

    int insertUser(User user);

    User findByToken(@Param("token") String token);

    User findById(@Param("id")Integer id);

    User selectUserById(@Param("account_id") String account_id);

    int update(User user);
}
