package com.zyy.community.entity;

import lombok.Data;

@Data
public class Comment {
    private Long id;
    private String content;
    private Integer parent_id;
    private Integer type;
    private Integer commentator;
    private Long gmt_create;
    private Integer like_count;
}
