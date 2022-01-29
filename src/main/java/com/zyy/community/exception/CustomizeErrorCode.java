package com.zyy.community.exception;

/**
 *  异常信息枚举类
 *      多个枚举要加逗号做间隔
 */

public enum CustomizeErrorCode implements ICustomizeErrorCode {

    QUESTION_NOT_FOUND("你要找的问题不存在哦~", 4000),
    NO_LOGIN("你还没未登录哦~", 4001),
    SYS_ERROR("服务器发生故障，稍后再试哦~", 4002),
    NO_QUESTION_OR_COMMENT_SELECTED("还没选择要评论的问题或评论哦~", 4003),
    PARAM_TYPE_WRONG("评论类型错误或不存在哦~", 4004),
    COMMENT_NOT_FOUND("你要操作的评论不见了~", 4005),
    CONTENT_IS_EMPTY("内容不能为空哦~", 4006),
    READ_FAIL("没有阅读权限哦~", 4007),
    NOTIFICATION_NOT_FOUND("你要找的通知不存在哦~", 4008)
    ;

    private final String message;
    private final Integer code;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }


    CustomizeErrorCode(String message, Integer code) {
        this.message = message;
        this.code = code;
    }
}
