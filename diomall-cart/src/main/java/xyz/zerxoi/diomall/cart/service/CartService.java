package xyz.zerxoi.diomall.cart.service;

import xyz.zerxoi.diomall.cart.vo.Cart;

import java.util.concurrent.ExecutionException;

public interface CartService {
    Cart addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;
}
