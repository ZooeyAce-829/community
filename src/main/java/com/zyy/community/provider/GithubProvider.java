package com.zyy.community.provider;

import com.alibaba.fastjson.JSON;
import com.zyy.community.dto.AccessTokenDTO;
import com.zyy.community.dto.GithubUser;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpVersion;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class GithubProvider {
    /**
     * 获取 access_token
     */
    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .callTimeout(120, TimeUnit.SECONDS)
                .readTimeout(1000, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS).build();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String str = response.body().string();
            String access_token = str.split("&")[0].split("=")[1];
            //log.info(access_token);
            return access_token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  获取用户
     */
    public GithubUser getUser(String access_token) {
        int timeout = 60;
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(timeout * 1000)
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .build();

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet("https://api.github.com/user");
        httpGet.addHeader(new BasicHeader("Authorization", "token " + access_token));
        httpGet.setProtocolVersion(HttpVersion.HTTP_1_0);
        httpGet.addHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE);
        httpGet.setConfig(defaultRequestConfig);
        CloseableHttpResponse response = null;
        GithubUser githubUser = null;

        try {
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            githubUser = JSON.parseObject(entity.getContent(), GithubUser.class);
            EntityUtils.consume(entity);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return githubUser;

/*        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .callTimeout(120, TimeUnit.SECONDS)
                .readTimeout(1000, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS).build();
        GithubUser githubUser = null;
        Request request = new Request.Builder()
                .url("https://api.github.com/user")
                .header("Authorization", "token " + access_token)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String str = response.body().string();
            // parseObject() 将字符串转换为指定类型的对象
            githubUser = JSON.parseObject(str, GithubUser.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return githubUser;*/
    }

}
