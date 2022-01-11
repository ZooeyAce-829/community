package com.zyy.community.exception;

/**
 *  异常信息枚举类
 *      多个枚举要加逗号做间隔
 */

public enum CustomizeErrorCode implements ICustomizeErrorCode {

    //USER_NOT_FOUND("USER NOT FOUND"),
    QUESTION_NOT_FOUND("该问题不存在");

    private final String message;

    @Override
    public String getMessage() {
        return message;
    }

    CustomizeErrorCode(String message) {
        this.message = message;
    }
}
