package com.zyy.community.service;

import com.zyy.community.dto.CommentDTO;
import com.zyy.community.entity.Comment;

import java.util.List;

public interface CommentService {

    Integer insert(Comment comment);

    List<CommentDTO> listCommentsByQuestionId(Integer id);
}
