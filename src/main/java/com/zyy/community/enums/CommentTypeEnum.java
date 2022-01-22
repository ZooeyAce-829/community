package com.zyy.community.enums;

public enum CommentTypeEnum {

    QUESTION(1),
    COMMENT(0);

    private final Integer type;

    CommentTypeEnum(Integer type) {
        this.type = type;
    }

    /**
     *  判断评论类型是否存在
     */
    public static boolean isExist(Integer type) {
        for (CommentTypeEnum value : CommentTypeEnum.values()) {
            if (value.type.equals(type)) {
                return true;
            }
        }
        return false;
    }

    public Integer getType() {
        return type;
    }
}
