package com.zyy.community.intereptor;

import com.zyy.community.dao.UserDao;
import com.zyy.community.entity.User;
import com.zyy.community.service.NotificationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class SessionInterceptor implements HandlerInterceptor {

    @Resource
    private UserDao userDao;

    @Resource
    private NotificationService notificationService;

    /**
     * session拦截器 (抽离出添加session的功能
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName()) && StringUtils.isNotBlank(cookie.getValue())) {
                    String token = cookie.getValue();
                    User user = userDao.findByToken(token);
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                        // session加入未读通知数
                        int notReadNotificationCount = notificationService.getNotReadCount(user.getId());
                        request.getSession().setAttribute("notReadNotificationCount", notReadNotificationCount);
                    }
                    break;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
