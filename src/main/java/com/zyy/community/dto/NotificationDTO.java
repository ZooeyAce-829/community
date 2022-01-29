package com.zyy.community.dto;

import lombok.Data;

@Data
public class NotificationDTO {
    private Integer id;
    private Long gmt_create;
    private Integer status;
    private Integer sender;
    private String sender_name;
    private Integer outer_id;
    private String outer_title;
    private String textType; // 回复问题/给你点赞/评论了你....
    private Integer type; // 区分回复类型
}
