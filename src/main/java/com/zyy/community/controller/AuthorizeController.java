package com.zyy.community.controller;

import com.zyy.community.dao.UserDao;
import com.zyy.community.dto.AccessTokenDTO;
import com.zyy.community.dto.GithubUser;
import com.zyy.community.entity.User;
import com.zyy.community.provider.GithubProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
@Slf4j
public class AuthorizeController {

    // 注入
    @Resource
    private GithubProvider provider;

    @Resource
    private UserDao userDao;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.client.uri}")
    private String uri;

    /**
     * 登录成功之后的跳转
     */
    @GetMapping(value = "/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request) {

        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(uri);
        accessTokenDTO.setState(state);
        String accessToken = provider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = provider.getUser(accessToken);
        if (null != githubUser) {
            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            user.setName(githubUser.getName());
            user.setAccount_id(String.valueOf(githubUser.getId()));
            user.setGmt_create(System.currentTimeMillis());
            user.setGmt_modify(user.getGmt_create());
            userDao.insertUser(user);
            request.getSession().setAttribute("user", githubUser);
            return "redirect:/";
        } else {
            // 登录失败
            return "redirect:/";
        }
    }

}
