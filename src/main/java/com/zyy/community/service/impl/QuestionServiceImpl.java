package com.zyy.community.service.impl;

import com.zyy.community.dao.QuestionDao;
import com.zyy.community.dao.UserDao;
import com.zyy.community.dto.PaginationDTO;
import com.zyy.community.dto.QuestionDTO;
import com.zyy.community.entity.Question;
import com.zyy.community.entity.User;
import com.zyy.community.exception.CustomizeErrorCode;
import com.zyy.community.exception.CustomizeException;
import com.zyy.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Resource
    private QuestionDao questionDao;

    @Resource
    private UserDao userDao;

    /**
     * @param page   当前页面
     * @param size   个数
     * @param search 搜索的输入内容
     * @param tag
     */
    @Override
    public PaginationDTO<QuestionDTO> listQuestions(Integer page, Integer size, String search, String tag) {

        if (StringUtils.isNotBlank(search)) {
            String[] keys = StringUtils.split(search, ",");
            search = (Arrays.stream(keys).collect(Collectors.joining("|"))).trim();
        }

        if (StringUtils.isBlank(search)) {
            search = null;
        }

        if (StringUtils.isBlank(tag)) {
            tag = null;
        }

        if (tag != null) {
            tag = tag.replace("+", "").replace("*", "")
                    .replace("?", "").replace("/", "");
        }

        // 数据库中总数据量
        int totalQuestions = questionDao.count(search, tag);

        // 总页数
        int pageCount = totalQuestions % size == 0 ? totalQuestions / size : totalQuestions / size + 1;

        // 清洗数据 page不合法时候重新赋值
        if (page > pageCount) {
            page = pageCount;
        }
        if (page < 1) {
            page = 1;
        }

        // 设置分页偏移量
        // SQL: select ... from ... limit offset,size;
        Integer offset = (page - 1) * size;

        // 数据库条件查询(limit)
        List<Question> questions = questionDao.listQuestions(offset, size, search, tag);
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<>();

        // questionDTO列表赋值
        for (Question question : questions) {
            // 根据question对象的creator拿到userid
            User user = userDao.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            // 调用工具类复制属性
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        // paginationDTO两个属性赋值，question属性和分页属性
        paginationDTO.setData(questionDTOList); // 构造器

        paginationDTO.setPagination(pageCount, page); // 自定义方法

        return paginationDTO;
    }

    @Override
    public PaginationDTO<QuestionDTO> listQuestionsByUserId(Integer userId, Integer page, Integer size) {

        // 数据库中总数据量
        Integer totalQuestions = questionDao.countByUserId(userId);
        // 总页数
        int pageCount;

        if (totalQuestions == 0) {
            pageCount = 1;
        } else {
            pageCount = totalQuestions % size == 0 ? totalQuestions / size : totalQuestions / size + 1;
        }

        if (page < 1) {
            page = 1;
        }
        if (page > pageCount) {
            page = pageCount;
        }

        Integer offset = (page - 1) * size;

        // 数据库条件查询(limit)
        List<Question> questions = questionDao.listQuestionsByUserId(userId, offset, size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<>();

        // questionDTO列表赋值
        for (Question question : questions) {
            // 根据question对象的creator拿到userid
            User user = userDao.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            // 调用工具类复制属性
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }

        // paginationDTO两个属性赋值，question属性和分页属性
        paginationDTO.setData(questionDTOList); // 构造器

        paginationDTO.setPagination(pageCount, page); // 自定义方法

        return paginationDTO;
    }

    @Override
    public QuestionDTO getById(Integer id) {
        Question question = questionDao.getById(id);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        // question 赋值到 questionDTO
        BeanUtils.copyProperties(question, questionDTO);
        questionDTO.setUser(userDao.findById(question.getCreator()));
        return questionDTO;
    }

    @Override
    public void createOrUpdate(Question question) {
        if (question.getId() == null) {
            // create
            question.setGmt_create(System.currentTimeMillis());
            question.setGmt_modify(question.getGmt_create());
            question.setComment_count(0);
            question.setLike_count(0);
            question.setView_count(0);
            questionDao.createQuestion(question);
        } else {
            // update
            question.setGmt_modify(question.getGmt_create());
            int count = questionDao.updateQuestion(question);
            if (count != 1) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    @Override
    public int incViewCount(QuestionDTO question) {
        return questionDao.updateViewCount(question);
    }

    @Override
    public List<QuestionDTO> getRelatedQuestions(QuestionDTO questionDTO) {

        if (StringUtils.isBlank(questionDTO.getTag())) {
            return new ArrayList<>();
        }

        String[] tags = questionDTO.getTag().split(",");

        String regexpTag = Arrays.stream(tags)
                .filter(StringUtils::isNotBlank)
                .map(t -> t.replace("+", "").replace("*", "")
                        .replace("?", "").replace("/", ""))
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining("|"));

        Question question = new Question();
        question.setId(questionDTO.getId());
        question.setTag(regexpTag);
        List<Question> questionList = questionDao.selectRelated(question);

        List<QuestionDTO> list = questionList.stream().map(i -> {
            QuestionDTO questionDTO1 = new QuestionDTO();
            BeanUtils.copyProperties(i, questionDTO1);
            return questionDTO1;
        }).collect(Collectors.toList());

        return list;
    }

}
