package com.zyy.community.controller;

import com.zyy.community.VO.UploadJsonResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FileUploadController {

    @PostMapping("/file/upload")
    @ResponseBody
    public UploadJsonResult doUpload() {
        return new UploadJsonResult(1, null, "/image/landscape.jpg");
    }
}
