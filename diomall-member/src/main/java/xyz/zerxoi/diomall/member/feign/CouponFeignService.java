package xyz.zerxoi.diomall.member.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import xyz.zerxoi.common.utils.R;

/**
 * @author zouxin <zouxin@kuaishou.com>
 * Created on 2021-08-13
 */
@FeignClient("diomall-coupon")
public interface CouponFeignService {
    @RequestMapping("/coupon/coupon/feign")
    R feign();
}
