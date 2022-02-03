package com.zyy.community.schedule.cache;

import com.zyy.community.dto.HotTagDTO;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Data
public class HotTagCache {
    private List<Map<String, String>> hotList = new ArrayList<>();

    public void updateTags(Map<String, Integer> tags) {
        int max = 5;
        PriorityQueue<HotTagDTO> priorityQueue = new PriorityQueue<>(max);
        List<Map<String, String>> sortedTags = new ArrayList<>();

        tags.forEach((tagName, tagPriority) -> {
            HotTagDTO hotTagDTO = new HotTagDTO(tagName, tagPriority);

            if (priorityQueue.size() < max) {
                priorityQueue.add(hotTagDTO);
            } else {
                // 获取最小优先级的元素
                HotTagDTO minPriorityTag = priorityQueue.peek();
                // 要保证当前元素是比父结点大的，保证小顶堆的特点
                if (hotTagDTO.compareTo(minPriorityTag) > 0) {
                    priorityQueue.poll();
                    priorityQueue.add(hotTagDTO);
                }
            }
        });

        while (!priorityQueue.isEmpty()) {
            Map<String, String> map = new HashMap<>();
            HotTagDTO poll = priorityQueue.poll();
            map.put("tagName", poll.getTagName());
            map.put("moreInfo", String.valueOf(poll.getTagPriority()));
            // 不断地头插，可以做到由大到小排序
            sortedTags.add(0, map);
        }
        hotList = sortedTags;
    }

}
