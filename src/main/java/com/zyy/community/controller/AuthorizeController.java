package com.zyy.community.controller;

import com.zyy.community.dto.AccessTokenDTO;
import com.zyy.community.provider.dto.GithubUser;
import com.zyy.community.entity.User;
import com.zyy.community.provider.GithubProvider;
import com.zyy.community.service.UserService;
import com.zyy.community.strategy.LoginUserInfo;
import com.zyy.community.strategy.UserStrategy;
import com.zyy.community.strategy.UserStrategyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
@Slf4j
public class AuthorizeController {

    // 工厂
    @Resource
    private UserStrategyFactory userStrategyFactory;

    @Resource
    private GithubProvider provider;

    @Resource
    private UserService userService;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.client.uri}")
    private String uri;

    /**
     * 重载的登录回调方法,开闭原则
     */
    @GetMapping(value = "/callback/{type}")
    public String callback(@PathVariable(name = "type") String type,
                           @RequestParam(name = "code") String code,
                           @RequestParam(name = "state", required = false) String state,
                           HttpServletResponse response) {

        UserStrategy userStrategy = userStrategyFactory.getStrategy(type);
        LoginUserInfo loginUserInfo = userStrategy.getUser(code, state);

        if (null != loginUserInfo && null != loginUserInfo.getId()) {
            User user = new User();

            // 将用户登录的账号信息赋值
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(loginUserInfo.getName());
            user.setAccount_id(String.valueOf(loginUserInfo.getId()));
            user.setAvatar_url(loginUserInfo.getAvatar_url());
            // set登录类型
            user.setAccount_type(type);

            userService.judgeByAccountId(user);

            Cookie cookie = new Cookie("token", token);
            cookie.setMaxAge(60 * 60 * 24 * 30 * 6);
            cookie.setPath("/");
            response.addCookie(cookie);
            return "redirect:/";
        } else {
            log.error("METHOD callback get user {} error", loginUserInfo);
            return "redirect:/";
        }
    }


    /**
     * 登录成功之后的跳转
     */
    @GetMapping(value = "/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response) {

        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();

        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(uri);
        accessTokenDTO.setState(state);

        String accessToken = provider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = provider.getUser(accessToken);

        if (null != githubUser && null != githubUser.getId()) {
            User user = new User();

            // 将用户登录的账号信息赋值
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccount_id(String.valueOf(githubUser.getId()));
            user.setAvatar_url(githubUser.getAvatar_url());

            userService.judgeByAccountId(user);

            Cookie cookie = new Cookie("token", token);
            cookie.setMaxAge(60 * 60 * 24 * 30 * 6);
            cookie.setPath("/");
            response.addCookie(cookie);
            return "redirect:/";
        } else {
            // 登录失败,追加日志
            log.error("METHOD callback get githubUser {} error", githubUser);
            return "redirect:/";
        }
    }

    /**
     * 退出登录
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {

        // 清除session
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/";
    }

}
