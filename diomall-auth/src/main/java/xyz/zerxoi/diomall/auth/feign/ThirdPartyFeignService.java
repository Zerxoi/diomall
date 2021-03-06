package xyz.zerxoi.diomall.auth.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.zerxoi.common.utils.R;

@FeignClient("diomall-third-party")
public interface ThirdPartyFeignService {

    @RequestMapping("/sms/sendSms")
    R sendSms(@RequestParam("phoneNum") String phoneNum, @RequestParam("code") String code);
}
