package com.zyy.community.service.impl;

import com.zyy.community.dao.CommentDao;
import com.zyy.community.dao.NotificationDao;
import com.zyy.community.dao.QuestionDao;
import com.zyy.community.dao.UserDao;
import com.zyy.community.dto.CommentDTO;
import com.zyy.community.entity.Comment;
import com.zyy.community.entity.Notification;
import com.zyy.community.entity.Question;
import com.zyy.community.entity.User;
import com.zyy.community.enums.CommentTypeEnum;
import com.zyy.community.enums.NotificationStatusEnum;
import com.zyy.community.enums.NotificationTypeEnum;
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

    @Resource
    private NotificationDao notificationDao;

    /**
     * 插入评论或子评论,并带有通知
     */
    @Override
    @Transactional
    public Integer insert(Comment comment, User commentator) {
        int flag;
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
                // 评论还在
                Question question = questionDao.getById(_comment.getParent_id());
                if (question == null) {
                    throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
                }

                flag = commentDao.insert(comment);

                // ##new) 顺带发通知
                getNotification(comment.getCommentator(), _comment.getCommentator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_TO_COMMENT, question.getId());

            }
        } else {
            // 回复问题
            // 判断问题是否还存在
            Question question = questionDao.getById(comment.getParent_id());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }

            flag = commentDao.insert(comment);
            // 对于问题插入的评论计入一次评论数
            questionDao.updateCommentCount(question);

            // 通知
            getNotification(comment.getCommentator(), question.getCreator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_TO_QUESTION, question.getId());

        }
        return flag;
    }

    /**
     * 根据id取Comment
     */
    @Override
    public List<CommentDTO> listCommentsById(Integer id, Integer type) {

        List<CommentDTO> commentList = commentDao.selectCommentById(id, type);

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

    /**
     * @param receiver 接收者
     * @param type     通知的类型 (枚举
     * @param outerId 点击跳转的时候需要一直拿到questionId，抽成变量，要不然当是子评论的时候拿到的是评论
     */
    private void getNotification(Integer sender, Integer receiver, String senderName, String outerTitle, NotificationTypeEnum type, Integer outerId) {
        Notification notification = new Notification();
        notification.setGmt_create(System.currentTimeMillis());
        notification.setSender(sender);
        notification.setReceiver(receiver);
        notification.setType(type.getType());
        notification.setStatus(NotificationStatusEnum.NOT_READ.getStatus());
        notification.setOuter_id(outerId);
        notification.setSender_name(senderName);
        notification.setOuter_title(outerTitle);

        Integer count = notificationDao.insertNotification(notification);
    }

}
