package xyz.zerxoi.diomall.cart.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.zerxoi.common.utils.R;
import xyz.zerxoi.diomall.cart.interceptor.CartInterceptor;
import xyz.zerxoi.diomall.cart.service.CartService;
import xyz.zerxoi.diomall.cart.vo.CartItem;
import xyz.zerxoi.diomall.cart.vo.UserInfoTo;

import javax.servlet.http.HttpSession;
import java.util.concurrent.ExecutionException;

@RestController
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart")
    public R cartListPage(HttpSession session) {
        UserInfoTo userInfoTo = CartInterceptor.getThreadLocal().get();
        return R.ok();
    }

    @GetMapping("/addToCart")
    public R addToCart(@RequestParam("skuId") Long skuId, @RequestParam("num") Integer num) throws ExecutionException
            , InterruptedException {
        CartItem cartItem = cartService.addToCart(skuId, num);
        return R.ok().put("data", cartItem);
    }
}
