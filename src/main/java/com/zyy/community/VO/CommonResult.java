package com.zyy.community.VO;

import com.zyy.community.exception.CustomizeErrorCode;
import com.zyy.community.exception.CustomizeException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  json发到前端的时候使用的结果集实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> {
    private Integer code;
    private String msg;
    private T data;

    public CommonResult(Integer code, String msg) {
        this(code, msg, null);
    }

    public static CommonResult errorOf(Integer code, String msg) {
        return new CommonResult(code, msg);
    }

    public static CommonResult errorOf(CustomizeErrorCode errorCode) {
        return errorOf(errorCode.getCode(), errorCode.getMessage());
    }

    public static CommonResult errorOf(CustomizeException e) {
        return errorOf(e.getCode(), e.getMessage());
    }

    public static CommonResult okOf() {
        return new CommonResult(2000, "ok");
    }

    public static <T> CommonResult okOf(T data) {
        return new CommonResult(2000, "ok", data);
    }

}
