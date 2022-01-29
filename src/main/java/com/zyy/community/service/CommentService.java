package com.zyy.community.service;

import com.zyy.community.dto.CommentDTO;
import com.zyy.community.entity.Comment;
import com.zyy.community.entity.User;

import java.util.List;

public interface CommentService {

    Integer insert(Comment comment, User commentator);

    List<CommentDTO> listCommentsById(Integer id, Integer type);
}
