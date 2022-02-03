package com.zyy.community.schedule;

import com.zyy.community.dto.QuestionDTO;
import com.zyy.community.schedule.cache.HotTagCache;
import com.zyy.community.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class HotTagTasks {

    @Resource
    private QuestionService questionService;

    @Resource
    private HotTagCache hotTagCache;

    /**
     * 定时器，开启定时任务
     */
    @Scheduled(fixedRate = 1000 * 60 * 60 * 3)
    public void reportCurrentTime() {

        int offset = 0;
        int size = 20;
        List<QuestionDTO> list = new ArrayList<>();

        Map<String, Integer> priorityMap = new HashMap<>();

        while (offset == 0 || list.size() == size) {
            list = questionService.listQuestions(offset, size, null, null).getData();
            for (QuestionDTO question : list) {

                String[] tags = StringUtils.split(question.getTag(), ",");

                for (String tag : tags) {
                    Integer priority = priorityMap.get(tag);
                    if (priority != null) {
                        priorityMap.put(tag, priority + 5 + question.getComment_count());
                    } else {
                        priorityMap.put(tag, 5 + question.getComment_count());
                    }
                }
            }
            offset += size;
        }

        hotTagCache.updateTags(priorityMap);
    }

}
