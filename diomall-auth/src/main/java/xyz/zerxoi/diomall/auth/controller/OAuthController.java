package xyz.zerxoi.diomall.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.zerxoi.common.utils.R;
import xyz.zerxoi.diomall.auth.service.GithubOAuthService;
import xyz.zerxoi.diomall.auth.vo.GithubAccessTokenVo;
import xyz.zerxoi.diomall.auth.vo.GithubUserVo;

@RestController
@RequestMapping("oauth")
public class OAuthController {
    private final GithubOAuthService githubOAuthService;

    public OAuthController(GithubOAuthService githubOAuthService) {
        this.githubOAuthService = githubOAuthService;
    }

    @GetMapping("/github")
    public R github(String code, String state) throws Exception {
        GithubAccessTokenVo accessToken = githubOAuthService.getAccessToken(code, state);
        if (accessToken != null) {
            R r = R.ok();
            GithubUserVo user = githubOAuthService.getUser(accessToken.getAccessToken());
            r.put("data", user);
            return r;
        }
        return R.error("获取Token失败");
    }
}
