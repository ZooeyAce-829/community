package com.zyy.community.service;

import com.zyy.community.dto.PaginationDTO;
import com.zyy.community.dto.QuestionDTO;
import com.zyy.community.entity.Question;

import java.util.List;

public interface QuestionService {

    PaginationDTO<QuestionDTO> listQuestions(Integer page, Integer size);

    PaginationDTO<QuestionDTO> listQuestionsByUserId(Integer id, Integer page, Integer size);

    QuestionDTO getById(Integer id);

    void createOrUpdate(Question question);

    int incViewCount(QuestionDTO question);

    List<QuestionDTO> getRelatedQuestions(QuestionDTO question);
}
