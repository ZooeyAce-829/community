package com.zyy.community.controller;

import com.zyy.community.dto.NotificationDTO;
import com.zyy.community.entity.User;
import com.zyy.community.enums.NotificationTypeEnum;
import com.zyy.community.service.NotificationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {

    @Resource
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String doNotify(HttpServletRequest request, @PathVariable(name = "id") Integer id) {

        User user = (User) request.getSession().getAttribute("user");
        if (null == user) return "redirect:/";

        NotificationDTO notificationDTO = notificationService.read(id, user);

        if (NotificationTypeEnum.REPLY_TO_COMMENT.getType().equals(notificationDTO.getType())
                || NotificationTypeEnum.REPLY_TO_QUESTION.getType().equals(notificationDTO.getType()))
            return "redirect:/question/" + notificationDTO.getOuter_id();

        return "redirect:/";
    }

}
