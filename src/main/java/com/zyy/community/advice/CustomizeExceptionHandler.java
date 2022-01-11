package com.zyy.community.advice;

import com.zyy.community.exception.CustomizeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 *  异常处理器，处理500等服务器的异常，404等无法路由的请求已经在Controller处理过了。
 *    ControllerAdvice注解 与 @ExceptionHandler(Exception.class)配合使用，将返回给controller的异常处理并返回页面
 */

@ControllerAdvice
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)
    ModelAndView handle(Throwable e) {
        ModelAndView mv = new ModelAndView();

        if (e instanceof CustomizeException) {
            mv.addObject("message", e.getMessage());
        } else {
            mv.addObject("message", "服务器发生异常");
        }

        mv.setViewName("error");
        return mv;
    }
}
