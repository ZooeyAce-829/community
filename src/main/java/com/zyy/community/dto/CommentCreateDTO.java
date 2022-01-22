package com.zyy.community.dto;

import lombok.Data;

/**
 *  仅仅是Comment实体类
 */

@Data
public class CommentCreateDTO {
    private Integer parent_id;
    private String content;
    private Integer type;

}
