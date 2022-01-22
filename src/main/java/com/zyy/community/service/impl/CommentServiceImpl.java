package com.zyy.community.service.impl;

import com.zyy.community.dao.CommentDao;
import com.zyy.community.dao.QuestionDao;
import com.zyy.community.dao.UserDao;
import com.zyy.community.dto.CommentDTO;
import com.zyy.community.entity.Comment;
import com.zyy.community.entity.Question;
import com.zyy.community.entity.User;
import com.zyy.community.enums.CommentTypeEnum;
import com.zyy.community.exception.CustomizeErrorCode;
import com.zyy.community.exception.CustomizeException;
import com.zyy.community.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentDao commentDao;

    @Resource
    private QuestionDao questionDao;

    @Resource
    private UserDao userDao;

    @Override
    @Transactional
    public Integer insert(Comment comment) {
        int count;
        if (comment.getParent_id() == null || comment.getParent_id() == 0) {
            throw new CustomizeException(CustomizeErrorCode.NO_QUESTION_OR_COMMENT_SELECTED);
        }

        // type不对
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.PARAM_TYPE_WRONG);
        }

        if (comment.getType().equals(CommentTypeEnum.COMMENT.getType())) {
            // 回复评论，做子评论
            // 看当前的父评论是否存在
            Comment _comment = commentDao.findByParentId(comment.getParent_id());
            if (_comment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            } else {
                count = commentDao.insert(comment);
            }
        } else {
            // 回复问题
            // 判断问题是否还存在
            Question question = questionDao.getById(comment.getParent_id());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }

            count = commentDao.insert(comment);
            // 对于问题插入的评论计入一次评论数
            questionDao.updateCommentCount(question);
        }
        return count;
    }

    /**
     * 根据questionId取Comment
     */
    @Override
    public List<CommentDTO> listCommentsByQuestionId(Integer id) {

        List<CommentDTO> commentList = commentDao.selectCommentByQuestionId(id);

        if (commentList.size() == 0) return new ArrayList<>();

        // Java8 stream()
        // set 对评论者id去重
        Set<Integer> commentator = commentList.stream().map(commentDTO -> commentDTO.getCommentator()).collect(Collectors.toSet());

        List<Integer> commentatorList = new ArrayList<>(commentator);

        // 根据idList查出所有user，并转化为map
        List<User> userList = userDao.selectUserByCommentator(commentatorList);
        Map<Integer, User> userMap = userList.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        // 将commentList里每一个comment映射为commentDTO再返回
        List<CommentDTO> commentDTOs = commentList.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOs;
    }
}
