package com.zyy.community.dao;

import com.zyy.community.dto.CommentDTO;
import com.zyy.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentDao {

    Integer insert(Comment comment);

    Comment findByParentId(Integer parent_id);

    List<CommentDTO> selectCommentByQuestionId(Integer id);
}