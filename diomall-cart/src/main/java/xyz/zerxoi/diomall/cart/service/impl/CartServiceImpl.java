package xyz.zerxoi.diomall.cart.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import xyz.zerxoi.common.constant.CartConstant;
import xyz.zerxoi.common.utils.R;
import xyz.zerxoi.diomall.cart.feign.ProductFeignService;
import xyz.zerxoi.diomall.cart.interceptor.CartInterceptor;
import xyz.zerxoi.diomall.cart.service.CartService;
import xyz.zerxoi.diomall.cart.vo.Cart;
import xyz.zerxoi.diomall.cart.vo.CartItem;
import xyz.zerxoi.diomall.cart.vo.SkuInfoVo;
import xyz.zerxoi.diomall.cart.vo.UserInfoTo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Service
public class CartServiceImpl implements CartService {
    private final StringRedisTemplate redisTemplate;
    private final ProductFeignService productFeignService;
    private final ThreadPoolExecutor executor;

    public CartServiceImpl(StringRedisTemplate redisTemplate, ProductFeignService productFeignService,
                           ThreadPoolExecutor executor) {
        this.redisTemplate = redisTemplate;
        this.productFeignService = productFeignService;
        this.executor = executor;
    }

    @Override
    public Cart addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        CartItem cartItem = new CartItem();
        CompletableFuture<Void> skuInfoTask = CompletableFuture.runAsync(() -> {
            R info = productFeignService.getSkuInfo(skuId);
            SkuInfoVo skuInfo = (SkuInfoVo) info.get("skuInfo");
            cartItem.setCheck(true);
            cartItem.setCount(num);
            cartItem.setImage(skuInfo.getSkuDefaultImg());
            cartItem.setTitle(skuInfo.getSkuTitle());
            cartItem.setSkuId(skuId);
            cartItem.setPrice(skuInfo.getPrice());
        }, executor);
        CompletableFuture<Void> skuAttrValueTask = CompletableFuture.runAsync(() -> {
            cartItem.setSkuAttr(productFeignService.getSkuSaleAttrValue(skuId));
        }, executor);
        CompletableFuture.allOf(skuInfoTask, skuAttrValueTask).get();

        cartOps.put(skuId, JSON.toJSON(cartItem));
        return null;
    }

    private BoundHashOperations<String, Object, Object> getCartOps() {
        UserInfoTo userInfoTo = CartInterceptor.getThreadLocal().get();
        String cartKey = CartConstant.REDIS_CART_PREFIX + (userInfoTo.getUserId() != null ?
                Long.toString(userInfoTo.getUserId()) : userInfoTo.getUserKey());
        // 绑定Hash操作的key
        return redisTemplate.boundHashOps(cartKey);
    }
}
