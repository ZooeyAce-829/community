package com.zyy.community.controller;

import com.zyy.community.dto.PaginationDTO;
import com.zyy.community.dto.QuestionDTO;
import com.zyy.community.schedule.cache.HotTagCache;
import com.zyy.community.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {

    @Resource
    private QuestionService questionService;

    @Resource
    private HotTagCache hotTagCache;

    /**
     * 首页默认路由
     * --在渲染出页面之前 去数据库拿到questionList和user信息
     */
    @GetMapping(value = "/")
    public String index(
            Model model,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "6") Integer size,
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "tag", required = false) String tag) {

        // 渲染文章列表
        // 这里不仅需要Question信息，还需要User的avatar_url信息，在QuestionDTO中封装需要的信息
        PaginationDTO<QuestionDTO> pageInfo = questionService.listQuestions(page, size, search, tag);

        List<Map<String, String>> hotList = hotTagCache.getHotList();
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("search", search);
        model.addAttribute("hotList", hotList);
        model.addAttribute("tag", tag);
        return "index";
    }

}
