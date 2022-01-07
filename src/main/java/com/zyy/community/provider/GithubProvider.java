package com.zyy.community.provider;

import com.alibaba.fastjson.JSON;
import com.zyy.community.dto.AccessTokenDTO;
import com.zyy.community.dto.GithubUser;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@Slf4j
public class GithubProvider {
    /**
     * 返回 access_token
     */
    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String str = response.body().string();
            String access_token = str.split("&")[0].split("=")[1];

            //log.info(str);  // access_token=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&scope=&token_type=bearer
            return access_token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public GithubUser getUser(String access_token) {
        OkHttpClient client = new OkHttpClient();
        GithubUser githubUser = null;
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + access_token)
                .addHeader("Authorization", "token" + access_token)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String str = response.body().string();
            // parseObject() 将字符串转换为指定类型的对象
            githubUser = JSON.parseObject(str, GithubUser.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return githubUser;
    }

}
