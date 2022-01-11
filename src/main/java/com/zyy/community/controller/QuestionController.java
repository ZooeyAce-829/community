package com.zyy.community.controller;

import com.zyy.community.dto.QuestionDTO;
import com.zyy.community.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;

@Controller
@Slf4j
public class QuestionController {

    @Resource
    private QuestionService questionService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Integer id, Model model) {
        QuestionDTO question = questionService.getById(id);
        // 模拟阅读量增加，访问一次增加一次
        int count = questionService.incViewCount(id);
        log.info(count == 1 ? "浏览量+1" : "操作失败");
        model.addAttribute("question", question);
        return "question";
    }
}
