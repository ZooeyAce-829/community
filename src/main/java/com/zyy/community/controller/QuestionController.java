package com.zyy.community.controller;

import com.zyy.community.dto.CommentDTO;
import com.zyy.community.dto.QuestionDTO;
import com.zyy.community.entity.Question;
import com.zyy.community.enums.CommentTypeEnum;
import com.zyy.community.service.CommentService;
import com.zyy.community.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class QuestionController {

    @Resource
    private QuestionService questionService;

    @Resource
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Integer id, Model model) {
        QuestionDTO question = questionService.getById(id);

        // 相关问题
        List<QuestionDTO> relatedQuestion = questionService.getRelatedQuestions(question);

        // 获取comment
        List<CommentDTO> commentsList = commentService.listCommentsById(id, CommentTypeEnum.QUESTION.getType());

        // 模拟阅读量增加，访问一次增加一次
        int count = questionService.incViewCount(question);
        model.addAttribute("question", question);
        model.addAttribute("comments", commentsList);
        model.addAttribute("relatedQuestions", relatedQuestion);
        return "question";
    }
}
