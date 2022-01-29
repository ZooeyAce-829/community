package com.zyy.community.dto;

import com.zyy.community.entity.User;
import lombok.Data;

/**
 *  关联User和Question实体
 */

@Data
public class QuestionDTO {
    private Integer id;
    private String title;
    private String description;  // 用数组会便于获取的时候截取
    private String tag;
    private Long gmt_create;
    private Long gmt_modify;
    private Integer creator;
    private Integer view_count;
    private Integer comment_count;
    private Integer like_count;
    // 通过user拿avatar_url
    private User user;
}
