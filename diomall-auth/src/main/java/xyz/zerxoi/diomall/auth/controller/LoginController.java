package xyz.zerxoi.diomall.auth.controller;

import lombok.Data;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import xyz.zerxoi.common.utils.R;
import xyz.zerxoi.diomall.auth.feign.ThirdPartyFeignService;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static xyz.zerxoi.common.constant.AuthConstant.SMS_CODE_CACHE_PREFIX;

@Data
@Controller
public class LoginController {
    private final ThirdPartyFeignService thirdPartyFeignService;
    private final StringRedisTemplate redisTemplate;

    public LoginController(ThirdPartyFeignService thirdPartyFeignService, StringRedisTemplate redisTemplate) {
        this.thirdPartyFeignService = thirdPartyFeignService;
        this.redisTemplate = redisTemplate;
    }

    // 刷新时间和过期时间分离
    @GetMapping("sendCode")
    public R sendCode(String phoneNum) {
        // TODO: 接口防刷
        String codeTimestamp = redisTemplate.opsForValue().get(SMS_CODE_CACHE_PREFIX + phoneNum);
        // 防止刷新（60s后刷新）
        if (StringUtils.isNotEmpty(codeTimestamp)) {
            long timestamp = Long.parseLong(codeTimestamp.split(":")[1]);
            if (System.currentTimeMillis() - timestamp < 60000) {
                return R.error("验证码获取过于频繁");
            }
        }
        String code = UUID.randomUUID().toString().substring(0, 6);
        redisTemplate.opsForValue().set(SMS_CODE_CACHE_PREFIX + phoneNum, code + ":" + System.currentTimeMillis(), 5, TimeUnit.MINUTES);

        thirdPartyFeignService.sendSms(phoneNum, code);
        return R.ok();
    }

    // 刷新时间和过期时间一致
    @GetMapping("sendCodeV2")
    public R sendCodeV2(String phoneNum) {
        String code = UUID.randomUUID().toString().substring(0, 6);
        // 过期后刷新
        Boolean absent = redisTemplate.opsForValue().setIfAbsent(SMS_CODE_CACHE_PREFIX + phoneNum, code, 5, TimeUnit.MINUTES);
        if (Boolean.TRUE.equals(absent)) {
            thirdPartyFeignService.sendSms(phoneNum, code);
            return R.ok();
        }
        return R.error("验证码获取过于频繁");
    }

    // code 校验应该在登陆中校验
    @GetMapping("validCode")
    public R validCode(String phoneNum, String code) {
        String codeTimestamp = redisTemplate.opsForValue().get(SMS_CODE_CACHE_PREFIX + phoneNum);
        if (StringUtils.isNotEmpty(codeTimestamp) && codeTimestamp.split(":")[0].equals(code)) {
            redisTemplate.delete(SMS_CODE_CACHE_PREFIX + phoneNum);
            return R.ok("验证码校验通过");
        }
        return R.error("验证码校验失败");
    }

    // code 校验应该在登陆中校验
    @GetMapping("validCodeV2")
    public R validCodeV2(String phoneNum, String code) {
        String cache = redisTemplate.opsForValue().get(SMS_CODE_CACHE_PREFIX + phoneNum);
        if (code.equals(cache)) {
            redisTemplate.delete(SMS_CODE_CACHE_PREFIX + phoneNum);
            return R.ok("验证码校验通过");
        }
        return R.error("验证码校验失败");
    }
}
