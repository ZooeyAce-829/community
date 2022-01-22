package com.zyy.community.dto;

import com.zyy.community.entity.User;
import lombok.Data;

/**
 *  展示Comment时候使用的实体类
 */

@Data
public class CommentDTO {
    private Long id;
    private String content;
    private Integer parent_id;
    private Integer type;
    private Integer commentator;
    private Long gmt_create;
    private Integer like_count;

    private User user;
}
