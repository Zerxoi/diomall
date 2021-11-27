package xyz.zerxoi.diomall.cart.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import xyz.zerxoi.common.constant.AuthConstant;
import xyz.zerxoi.common.constant.CartConstant;
import xyz.zerxoi.common.vo.MemberVo;
import xyz.zerxoi.diomall.cart.vo.UserInfoTo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

// 在执行方法之前判断用户登陆状态，处理请求后将封装后的结果传递给 controller
@Component
public class CartInterceptor implements HandlerInterceptor {
    // 在拦截器中设置ThreadLocal，可以在请求整个过程到 preHandle -> 业务代码 -> postHandle 整个流程通过 ThreadLocal 获取对象
    private static final ThreadLocal<UserInfoTo> threadLocal = new ThreadLocal<>();

    public static ThreadLocal<UserInfoTo> getThreadLocal() {
        return threadLocal;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        UserInfoTo userInfoTo = new UserInfoTo();
        HttpSession session = request.getSession();
        MemberVo member = (MemberVo) session.getAttribute(AuthConstant.LOGIN_USER);
        if (member != null) {
            // 用户未登录
            userInfoTo.setUserId(member.getId());
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(CartConstant.TEMP_USER_COOKIE)) {
                    userInfoTo.setUserKey(cookie.getValue());
                    userInfoTo.setUserKeyInCookie(true);
                }
            }
        }
        if (StringUtils.isEmpty(userInfoTo.getUserKey())) {
            String userKey = UUID.randomUUID().toString();
            userInfoTo.setUserKey(userKey);
        }

        // 放入 ThreadLocal
        threadLocal.set(userInfoTo);

        // 设置 Cookie
        if (!userInfoTo.isUserKeyInCookie()) {
            Cookie cookie = new Cookie(CartConstant.TEMP_USER_COOKIE, userInfoTo.getUserKey());
            cookie.setMaxAge(CartConstant.COOKIE_MAX_AGE);
            cookie.setDomain("diomall.com");
            response.addCookie(cookie);
        }
        // 全部放行
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
        // postHandle 对于@ResponseBody 和 ResponseEntity 方法的用处不大
        // 在这些方法中，响应是在 HandlerAdapter 内和 postHandle 之前写入和提交的
        // 这意味着对响应进行任何更改（例如添加额外的标头）为时已晚
        // if (!threadLocal.get().isUserKeyInCookie()) {
        //     Cookie cookie = new Cookie(CartConstant.TEMP_USER_COOKIE, threadLocal.get().getUserKey());
        //     cookie.setMaxAge(CartConstant.COOKIE_MAX_AGE);
        //     cookie.setDomain("diomall.com");
        //     response.addCookie(cookie);
        // }
    }
}
