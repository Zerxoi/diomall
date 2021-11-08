package xyz.zerxoi.diomall.ssoserver.controller;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.zerxoi.common.utils.R;
import xyz.zerxoi.diomall.ssoserver.vo.MemberVo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Controller
public class SsoController {
    private final StringRedisTemplate redisTemplate;

    public SsoController(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 登录页面
    @GetMapping("index.html")
    public String index(@RequestParam("redirect_url") String url,
                        @CookieValue(value = "sso_token", required = false) String ssoToken,
                        HttpServletResponse response) throws IOException {
        // Cookie存在sso_token表示已登录，直接跳转回重定向页面
        if (ssoToken != null) {
            response.sendRedirect(url + "?sso_token=" + ssoToken);
            return null;
        }
        // 否则返回登陆页面
        return "index.html";
    }

    // 登录
    @GetMapping("login")
    public R login(@RequestParam("username") String username,
                   @RequestParam("password") String password,
                   @RequestParam("redirect_url") String url,
                   @CookieValue(value = "sso_token", required = false) String ssoToken,
                   HttpServletResponse response) throws IOException {
        // 如果cookie中有Token表示登录过，直接返回
        if (ssoToken != null) {
            return R.ok("用户已登录，请先登出用户");
        }

        // 没有登录，验证账号密码，设置Token，将账号数据以Token为键保存至缓存并将Token保存至cookie，最后转发至原URL
        if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) {
            // 查数据库获取数据并放入 redis
            // TODO cookie和缓存的过期
            ssoToken = UUID.randomUUID().toString();
            MemberVo memberVo = new MemberVo();
            memberVo.setNickname("zouxin");
            redisTemplate.opsForValue().set(ssoToken, JSON.toJSONString(memberVo));
            response.addCookie(new Cookie("sso_token", ssoToken));
            response.sendRedirect(url + "?sso_token=" + ssoToken);
            return null;
        }
        return R.error("用户名或密码错误");
    }

    @ResponseBody
    @GetMapping("user")
    public MemberVo user(@RequestParam("sso_token") String ssoToken) {
        return JSON.parseObject(redisTemplate.opsForValue().get(ssoToken), MemberVo.class);
    }
}
