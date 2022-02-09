package com.zyy.community.dao;

import com.zyy.community.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDao {

    int insertUser(User user);

    User findByToken(@Param("token") String token);

    User findById(@Param("id")Integer id);

    User selectUserById(@Param("account_id") String account_id, @Param("account_type") String account_type);

    int update(User user);

    List<User> selectUserByCommentator(@Param("commentatorList") List<Integer> commentatorList);
}
