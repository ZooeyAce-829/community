package com.zyy.community.VO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadJsonResult {
    // 0:fail  |  1:success
    private Integer success;

    // msg
    private String message;

    // image url when success
    private String url;
}
