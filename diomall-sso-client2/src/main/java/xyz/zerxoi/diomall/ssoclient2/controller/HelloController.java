package xyz.zerxoi.diomall.ssoclient2.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import xyz.zerxoi.common.utils.R;
import xyz.zerxoi.diomall.ssoclient2.vo.MemberVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
public class HelloController {

    @Value("${diomall.sso.server}")
    private String ssoServer;

    @GetMapping("hello")
    public R hello(@RequestParam(value = "sso_token", required = false) String ssoToken,
                   HttpSession session,
                   HttpServletRequest request,
                   HttpServletResponse response) throws IOException {
        MemberVo memberVo = (MemberVo) session.getAttribute("ssoLogin");
        if (memberVo == null) {
            if (ssoToken != null) {
                ResponseEntity<MemberVo> entity = new RestTemplate().getForEntity(ssoServer + "/user?sso_token=" + ssoToken, MemberVo.class);
                memberVo = entity.getBody();
                session.setAttribute("ssoLogin", memberVo);
            } else {
                String redirectUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getRequestURI();
                response.sendRedirect(ssoServer + "/index.html" + "?redirect_url=" + redirectUrl);
                return null;
            }
        }

        return R.ok().put("data", "Hello, " + memberVo.getNickname());
    }

}
