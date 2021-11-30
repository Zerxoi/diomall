package xyz.zerxoi.diomall.product.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import xyz.zerxoi.common.utils.R;
import xyz.zerxoi.common.to.SkuReductionTo;
import xyz.zerxoi.common.to.SpuBoundsTo;

@FeignClient("diomall-coupon")
public interface CouponFeignService {

    /**
     * 保存spu的积分信息
     * @param spuBoundsTo
     */
    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundsTo spuBoundsTo);

    /**
     * 保存sku的优惠满减信息
     * @param skuReductionTo
     */
    @PostMapping("/coupon/skufullreduction/saveinfo")
    R saveSkuReductionTo(@RequestBody SkuReductionTo skuReductionTo);
}