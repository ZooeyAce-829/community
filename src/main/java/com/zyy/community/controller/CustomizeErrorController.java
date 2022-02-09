package com.zyy.community.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 *  处理404等异常
 */

@Controller
@RequestMapping("${server.error.path:${error.path:/error}}") // server.error.path默认值是error，代表这是专门处理error请求的controller
public class CustomizeErrorController implements ErrorController {

    @RequestMapping(produces = {"text/html"})
    public ModelAndView errorHtml(Model model, HttpServletRequest request) {
        HttpStatus status = getStatus(request);

        if (status.is4xxClientError()) {
            model.addAttribute("message", "请求的页面不存在");
        }
        if (status.is5xxServerError()) {
            model.addAttribute("message", "服务器压力有点大哦.....");
        }

        return new ModelAndView("error");
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            try {
                return HttpStatus.valueOf(statusCode);
            } catch (Exception var4) {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
    }

    @Override
    public String getErrorPath() {
        return "error";
    }
}
