package xyz.zerxoi.diomall.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import xyz.zerxoi.common.constant.CartConstant;
import xyz.zerxoi.common.utils.R;
import xyz.zerxoi.diomall.cart.feign.ProductFeignService;
import xyz.zerxoi.diomall.cart.interceptor.CartInterceptor;
import xyz.zerxoi.diomall.cart.service.CartService;
import xyz.zerxoi.diomall.cart.vo.Cart;
import xyz.zerxoi.diomall.cart.vo.CartItem;
import xyz.zerxoi.diomall.cart.vo.SkuInfoVo;
import xyz.zerxoi.diomall.cart.vo.UserInfoTo;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

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
    public CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        String cartItemString = (String) cartOps.get(skuId.toString());
        if (StringUtils.isEmpty(cartItemString)) {
            CartItem cartItem = new CartItem();
            CompletableFuture<Void> skuInfoTask = CompletableFuture.runAsync(() -> {
                R info = productFeignService.getSkuInfo(skuId);
                String jsonString = JSON.toJSONString(info.get("skuInfo"));
                SkuInfoVo skuInfo = JSON.parseObject(jsonString, new TypeReference<>() {
                });
                if (skuInfo != null) {
                    cartItem.setCheck(true);
                    cartItem.setCount(num);
                    cartItem.setImage(skuInfo.getSkuDefaultImg());
                    cartItem.setTitle(skuInfo.getSkuTitle());
                    cartItem.setSkuId(skuId);
                    cartItem.setPrice(skuInfo.getPrice());
                }
            }, executor);
            CompletableFuture<Void> skuAttrValueTask =
                    CompletableFuture.runAsync(() -> cartItem.setSkuAttr(productFeignService.getSkuSaleAttrValue(skuId)), executor);
            CompletableFuture.allOf(skuInfoTask, skuAttrValueTask).get();

            cartOps.put(skuId.toString(), JSON.toJSONString(cartItem));
            return cartItem;
        }
        CartItem cartItem = JSON.parseObject(cartItemString, CartItem.class);
        cartItem.setCount(cartItem.getCount() + num);
        cartOps.put(skuId.toString(), JSON.toJSONString(cartItem));
        return cartItem;
    }

    @Override
    public Cart getCart() throws ExecutionException, InterruptedException {
        Cart cart = new Cart();
        UserInfoTo userInfoTo = CartInterceptor.getThreadLocal().get();
        String tmpCartKey = CartConstant.REDIS_CART_PREFIX + userInfoTo.getUserKey();
        if (userInfoTo.getUserId() != null) { // 登录
            // 临时购物车添加到用户购物车
            List<CartItem> tempCart = getCartItems(tmpCartKey);
            if (!CollectionUtils.isEmpty(tempCart)) {
                for (CartItem cartItem : tempCart) {
                    addToCart(cartItem.getSkuId(), cartItem.getCount());
                }
                clearCart(tmpCartKey);
            }
            // 获取用户购物车
            String userCartKey = CartConstant.REDIS_CART_PREFIX + userInfoTo.getUserId();
            List<CartItem> cartItems = getCartItems(userCartKey);
            cart.setList(cartItems);
        } else { // 未登录
            cart.setList(getCartItems(tmpCartKey));
        }
        return cart;
    }

    @Override
    public void clearCart(String cartKey) {
        redisTemplate.delete(cartKey);
    }

    private BoundHashOperations<String, Object, Object> getCartOps() {
        UserInfoTo userInfoTo = CartInterceptor.getThreadLocal().get();
        String cartKey = CartConstant.REDIS_CART_PREFIX + (userInfoTo.getUserId() != null ?
                Long.toString(userInfoTo.getUserId()) : userInfoTo.getUserKey());
        // 绑定Hash操作的key
        return redisTemplate.boundHashOps(cartKey);
    }

    private List<CartItem> getCartItems(String cartKey) {
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(cartKey);
        List<Object> values = hashOps.values();
        if (!CollectionUtils.isEmpty(values)) {
            return values.stream().map(obj -> JSON.parseObject((String) obj, CartItem.class))
                    .collect(Collectors.toList());
        }
        return null;
    }
}
