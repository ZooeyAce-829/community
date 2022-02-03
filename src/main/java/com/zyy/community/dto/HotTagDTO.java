package com.zyy.community.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HotTagDTO implements Comparable {
    private String tagName;
    private Integer tagPriority;


    @Override
    public int compareTo(Object o) {
        // 比较结果会决定是大顶堆还是小顶堆，此时是小顶堆
        return this.getTagPriority() - ((HotTagDTO) o).getTagPriority();
    }
}
