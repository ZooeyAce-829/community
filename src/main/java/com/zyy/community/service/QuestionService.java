package com.zyy.community.service;

import com.zyy.community.dto.PaginationDTO;
import com.zyy.community.dto.QuestionDTO;
import com.zyy.community.entity.Question;

public interface QuestionService {

    PaginationDTO listQuestions(Integer page, Integer size);

    PaginationDTO listQuestionsByUserId(Integer id, Integer page, Integer size);

    QuestionDTO getById(Integer id);

    void createOrUpdate(Question question);

    int incViewCount(QuestionDTO question);

}
