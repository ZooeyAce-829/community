package com.zyy.community.advice;

import com.alibaba.fastjson.JSON;
import com.zyy.community.VO.CommonResult;
import com.zyy.community.exception.CustomizeErrorCode;
import com.zyy.community.exception.CustomizeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 异常处理器，处理500等服务器的异常，404等无法路由的请求已经在Controller处理过了。
 * ControllerAdvice注解 与 @ExceptionHandler(Exception.class)配合使用，将返回给controller的异常处理并返回页面
 */

@ControllerAdvice
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)
    ModelAndView handle(Throwable e, HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView();
        CommonResult result;

        if ("application/json".equals(request.getContentType())) {
            // json
            if (e instanceof CustomizeException) {
                result = CommonResult.errorOf((CustomizeException) e);
            } else {
                result = CommonResult.errorOf(CustomizeErrorCode.SYS_ERROR);
            }

            response.setContentType("application/json");
            response.setStatus(200);
            response.setCharacterEncoding("utf-8");
            try {
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(result));
                writer.close();
            } catch (IOException ioException) {

            }

            return null;
        } else {
            // 页面
            if (e instanceof CustomizeException) {
                mv.addObject("message", e.getMessage());
            } else {
                mv.addObject("message", CustomizeErrorCode.SYS_ERROR.getMessage());
            }
            mv.setViewName("error");
            return mv;
        }

    }
}
