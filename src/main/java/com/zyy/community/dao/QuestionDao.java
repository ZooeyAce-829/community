package com.zyy.community.dao;

import com.zyy.community.dto.QuestionDTO;
import com.zyy.community.entity.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QuestionDao {

    Integer createQuestion(Question question);

    List<Question> listQuestions(Integer offset, Integer size);

    Integer count();

    Integer countByUserId(@Param(value = "userId") Integer userId);

    List<Question> listQuestionsByUserId(@Param(value = "userId") Integer userId, Integer offset, Integer size);

    Question getById(@Param(value = "id") Integer id);

    Integer updateQuestion(Question question);

    Integer updateViewCount(QuestionDTO question);

    int updateCommentCount(Question question);
}
