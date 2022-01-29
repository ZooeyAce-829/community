package com.zyy.community.service;

import com.zyy.community.dto.NotificationDTO;
import com.zyy.community.dto.PaginationDTO;
import com.zyy.community.entity.User;

public interface NotificationService {

    PaginationDTO<NotificationDTO> list(Integer id, Integer page, Integer size);

    Integer getNotReadCount(Integer userId);

    NotificationDTO read(Integer id, User user);
}
