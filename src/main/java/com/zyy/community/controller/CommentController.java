package com.zyy.community.controller;

import com.zyy.community.VO.CommonResult;
import com.zyy.community.dto.CommentCreateDTO;
import com.zyy.community.dto.CommentDTO;
import com.zyy.community.entity.Comment;
import com.zyy.community.entity.User;
import com.zyy.community.enums.CommentTypeEnum;
import com.zyy.community.exception.CustomizeErrorCode;
import com.zyy.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {

    @Resource
    private CommentService commentService;

    /**
     * 评论
     */
    @ResponseBody
    @PostMapping(value = "/comment")
    public Object comment(@RequestBody CommentCreateDTO commentCreateDTO,
                          HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return CommonResult.errorOf(CustomizeErrorCode.NO_LOGIN);
        }

        // 判空
        if (commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())) {
            return CommonResult.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }

        Comment comment = new Comment();
        comment.setParent_id(commentCreateDTO.getParent_id());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmt_create(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setLike_count(0);
        int count = commentService.insert(comment, user);

        return CommonResult.okOf();
    }

    /**
     * 子评论
     */
    @ResponseBody
    @GetMapping(value = "/comment/{id}")
    public CommonResult<List<CommentDTO>> subComments(@PathVariable(name = "id") Integer id) {

        List<CommentDTO> subComments = commentService.listCommentsById(id, CommentTypeEnum.COMMENT.getType());
        return CommonResult.okOf(subComments);
    }

}
