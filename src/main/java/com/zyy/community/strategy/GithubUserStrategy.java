package com.zyy.community.strategy;

import com.zyy.community.dto.AccessTokenDTO;
import com.zyy.community.provider.GithubProvider;
import com.zyy.community.provider.dto.GithubUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class GithubUserStrategy implements UserStrategy {

    @Resource
    private GithubProvider provider;

    @Override
    public LoginUserInfo getUser(String code, String state) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);

        String accessToken = provider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = provider.getUser(accessToken);

        LoginUserInfo loginUserInfo = new LoginUserInfo();
        loginUserInfo.setId(githubUser.getId());
        loginUserInfo.setName(githubUser.getName());
        loginUserInfo.setBio(githubUser.getBio());
        loginUserInfo.setAvatar_url(githubUser.getAvatar_url());

        return loginUserInfo;
    }

    @Override
    public String getSupportedType() {
        return "github";
    }
}
