package com.zyy.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 页面的数据实体，包括question列表、页码
 */

@Data
public class PaginationDTO<T> {
    // question列表
    private List<T> data;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer currentPage;
    // 分页器保存页码的数组
    private List<Integer> pages = new ArrayList<>();
    private Integer pageCount;

    /**
     *
     * @param pageCount 总页数
     * @param page 当前页
     */
    public void setPagination(Integer pageCount, Integer page) {
        this.pageCount = pageCount;
        this.currentPage = page;

        pages.add(page);
        for (int i = 1; i <= 3; i++) {
            if (page - i > 0) {
                pages.add(0, page - i);
            }

            if (page + i <= pageCount) {
                pages.add(page + i);
            }
        }

        // 上一页按钮
        if (page == 1) {
            showPrevious = false;
        } else {
            showPrevious = true;
        }

        // 下一页按钮
        if (page == pageCount) {
            showNext = false;
        } else {
            showNext = true;
        }

        // 首页按钮
        if (pages.contains(1)) {
            showFirstPage = false;
        } else {
            showFirstPage = true;
        }

        // 尾页按钮
        if (pages.contains(pageCount)) {
            showEndPage = false;
        } else {
            showEndPage = true;
        }

    }
}
