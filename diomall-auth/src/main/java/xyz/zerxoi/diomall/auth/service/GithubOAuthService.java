package xyz.zerxoi.diomall.auth.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xyz.zerxoi.diomall.auth.utils.HttpUtils;
import xyz.zerxoi.diomall.auth.vo.GithubAccessTokenVo;
import xyz.zerxoi.diomall.auth.vo.GithubUserVo;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class GithubOAuthService {
    @Value("${oauth.github.client-id}")
    private String clientId;
    @Value("${oauth.github.client-secret}")
    private String clientSecret;
    @Value("${oauth.github.redirect-uri}")
    private String redirectUri;

    private final Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

    public GithubAccessTokenVo getAccessToken(String code, String state) throws Exception {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        HashMap<String, String> bodies = new HashMap<>();
        bodies.put("client_id", clientId);
        bodies.put("client_secret", clientSecret);
        bodies.put("redirect_uri", redirectUri);
        bodies.put("code", code);
        bodies.put("state", state);
        HttpResponse response = HttpUtils.doPost("https://github.com", "/login/oauth/access_token", headers, null, bodies);
        GithubAccessTokenVo accessTokenVo = null;
        if (response.getStatusLine().getStatusCode() == 200) {
            accessTokenVo = gson.fromJson(new InputStreamReader(response.getEntity().getContent()), GithubAccessTokenVo.class);
        }
        return accessTokenVo;
    }

    public GithubUserVo getUser(String accessToken) throws Exception {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "token " + accessToken);
        HttpResponse response = HttpUtils.doGet("https://api.github.com", "/user", headers, null);
        GithubUserVo userVo = null;
        if (response.getStatusLine().getStatusCode() == 200) {
            userVo = gson.fromJson(new InputStreamReader(response.getEntity().getContent()), GithubUserVo.class);
        }
        return userVo;
    }
}
