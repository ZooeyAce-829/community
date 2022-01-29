package com.zyy.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    private Integer id;
    private Integer sender;
    private String sender_name;
    private Integer receiver;
    private Integer type;
    private Long gmt_create;
    private Integer status;
    private Integer outer_id;
    private String outer_title;
}
