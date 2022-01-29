package com.zyy.community.enums;

public enum NotificationStatusEnum {

    NOT_READ(0),
    HAVE_READ(1)
    ;

    public Integer getStatus() {
        return status;
    }

    private Integer status;

    NotificationStatusEnum(Integer status) {
        this.status = status;
    }
}
