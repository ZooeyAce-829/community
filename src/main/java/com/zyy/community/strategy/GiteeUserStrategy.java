package com.zyy.community.strategy;

import com.zyy.community.dto.AccessTokenDTO;
import com.zyy.community.provider.GiteeProvider;
import com.zyy.community.provider.dto.GiteeUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class GiteeUserStrategy implements UserStrategy {

    @Resource
    private GiteeProvider provider;

    @Override
    public LoginUserInfo getUser(String code, String state) {

        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);

        String accessToken = provider.getAccessToken(accessTokenDTO);
        GiteeUser giteeUser = provider.getUser(accessToken);

        LoginUserInfo loginUserInfo = new LoginUserInfo();
        loginUserInfo.setId(giteeUser.getId());
        loginUserInfo.setName(giteeUser.getName());
        loginUserInfo.setBio(giteeUser.getBio());
        loginUserInfo.setAvatar_url(giteeUser.getAvatar_url());

        return loginUserInfo;
    }

    @Override
    public String getSupportedType() {
        return "gitee";
    }
}
