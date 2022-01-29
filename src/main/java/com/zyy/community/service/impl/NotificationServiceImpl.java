package com.zyy.community.service.impl;

import com.zyy.community.dao.NotificationDao;
import com.zyy.community.dto.NotificationDTO;
import com.zyy.community.dto.PaginationDTO;
import com.zyy.community.entity.Notification;
import com.zyy.community.entity.User;
import com.zyy.community.enums.NotificationStatusEnum;
import com.zyy.community.enums.NotificationTypeEnum;
import com.zyy.community.exception.CustomizeErrorCode;
import com.zyy.community.exception.CustomizeException;
import com.zyy.community.service.NotificationService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Resource
    private NotificationDao notificationDao;

    @Override
    public PaginationDTO<NotificationDTO> list(Integer userId, Integer page, Integer size) {

        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();

        // 数据库中总数据量
        Integer totalQuestions = notificationDao.countByUserId(userId);

        // 总页数
        int pageCount;

        if (totalQuestions == 0) {
            pageCount = 1;
        } else {
            pageCount = totalQuestions % size == 0 ? totalQuestions / size : totalQuestions / size + 1;
        }

        if (page < 1) {
            page = 1;
        }
        if (page > pageCount) {
            page = pageCount;
        }

        Integer offset = (page - 1) * size;

        // 数据库条件查询(limit)
        List<Notification> notifications = notificationDao.listNotificationsByUserId(userId, offset, size);

        if (notifications.size() == 0) return paginationDTO;

        List<NotificationDTO> notificationDTOList = new ArrayList<>();

        // 遍历查询到的通知，转换后加入新的集合返回
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            // 添加文本属性
            notificationDTO.setTextType(NotificationTypeEnum.nameOfType(notification.getType()));
            // 加进去
            notificationDTOList.add(notificationDTO);
        }

        paginationDTO.setData(notificationDTOList);
        paginationDTO.setPagination(pageCount, page); // 自定义方法
        return paginationDTO;
    }

    @Override
    public Integer getNotReadCount(Integer userId) {
        return notificationDao.selectNotReadCount(userId);
    }

    @Override
    public NotificationDTO read(Integer id, User user) {
        Notification notification = notificationDao.selectById(id);

        if (null == notification) {
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }

        if (!notification.getReceiver().equals(user.getId())) {
            throw new CustomizeException(CustomizeErrorCode.READ_FAIL);
        }

        // 标记已读
        notification.setStatus(NotificationStatusEnum.HAVE_READ.getStatus());
        Integer flag = notificationDao.updateNotificationStatus(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        // 设置额外的文本信息
        notificationDTO.setTextType(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;

    }
}
