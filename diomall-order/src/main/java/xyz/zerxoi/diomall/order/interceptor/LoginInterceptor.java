package xyz.zerxoi.diomall.order.interceptor;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import xyz.zerxoi.common.constant.AuthConstant;
import xyz.zerxoi.common.vo.MemberVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 登录拦截器
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Getter
    private static final ThreadLocal<MemberVo> memberVoThreadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        MemberVo memberVo = (MemberVo) request.getSession().getAttribute(AuthConstant.LOGIN_USER);
        if (memberVo == null) {
            response.sendRedirect("http://auth.diomall.com/login.html");
            return false;
        }
        memberVoThreadLocal.set(memberVo);
        return true;
    }
}
