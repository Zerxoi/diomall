package xyz.zerxoi.diomall.cart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import xyz.zerxoi.common.utils.R;
import xyz.zerxoi.diomall.cart.interceptor.CartInterceptor;
import xyz.zerxoi.diomall.cart.vo.UserInfoTo;

import javax.servlet.http.HttpSession;

@Controller
public class CartController {

    @GetMapping("/cart")
    public R cartListPage(HttpSession session) {
        UserInfoTo userInfoTo = CartInterceptor.getThreadLocal().get();
        return R.ok();
    }
}
