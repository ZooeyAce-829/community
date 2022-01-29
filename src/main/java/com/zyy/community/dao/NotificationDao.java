package com.zyy.community.dao;

import com.zyy.community.entity.Notification;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NotificationDao {

    Integer insertNotification(Notification notification);

    Integer countByUserId(@Param("userId") Integer userId);

    List<Notification> listNotificationsByUserId(@Param("userId") Integer userId, @Param("offset") Integer offset, @Param("size") Integer size);

    Integer selectNotReadCount(@Param("userId") Integer userId);

    Notification selectById(@Param("id") Integer id);

    Integer updateNotificationStatus(Notification notification);
}
