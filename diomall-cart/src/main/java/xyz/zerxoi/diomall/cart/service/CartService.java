package xyz.zerxoi.diomall.cart.service;

import xyz.zerxoi.diomall.cart.vo.Cart;
import xyz.zerxoi.diomall.cart.vo.CartItem;

import java.util.concurrent.ExecutionException;

public interface CartService {
    CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    Cart getCart() throws ExecutionException, InterruptedException;

    void clearCart(String cartKey);
}
