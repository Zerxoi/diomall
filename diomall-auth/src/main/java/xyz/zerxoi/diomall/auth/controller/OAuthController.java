package xyz.zerxoi.diomall.auth.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.zerxoi.common.constant.AuthConstant;
import xyz.zerxoi.common.utils.R;
import xyz.zerxoi.diomall.auth.feign.MemberFeignService;
import xyz.zerxoi.diomall.auth.service.GithubOAuthService;
import xyz.zerxoi.diomall.auth.vo.GithubAccessTokenVo;
import xyz.zerxoi.diomall.auth.vo.GithubUserVo;
import xyz.zerxoi.diomall.auth.vo.MemberVo;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("oauth")
public class OAuthController {
    private final GithubOAuthService githubOAuthService;
    private final MemberFeignService memberFeignService;

    public OAuthController(GithubOAuthService githubOAuthService, MemberFeignService memberFeignService) {
        this.githubOAuthService = githubOAuthService;
        this.memberFeignService = memberFeignService;
    }

    @GetMapping("/github/login")
    public R github(String code, String state, HttpSession session) throws Exception {
        GithubAccessTokenVo accessToken = githubOAuthService.getAccessToken(code, state);
        if (accessToken != null) {
            GithubUserVo githubUserVo = githubOAuthService.getUser(accessToken.getAccessToken());
            R login = memberFeignService.login(githubUserVo);
            if (login.getCode() == 0) {
                String jsonString = JSON.toJSONString(login.get("data"));
                MemberVo memberVo = JSON.parseObject(jsonString, new TypeReference<>() {
                });
                session.setAttribute(AuthConstant.LOGIN_USER, memberVo);
                return R.ok("登录成功（跳转至首页）");
            } else {
                return R.error("登录失败");
            }
        }
        return R.error("获取Token失败");
    }
}
