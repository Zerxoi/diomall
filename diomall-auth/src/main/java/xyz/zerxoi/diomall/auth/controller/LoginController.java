package xyz.zerxoi.diomall.auth.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import xyz.zerxoi.common.constant.AuthConstant;
import xyz.zerxoi.common.utils.R;
import xyz.zerxoi.common.vo.MemberVo;
import xyz.zerxoi.diomall.auth.feign.MemberFeignService;
import xyz.zerxoi.diomall.auth.feign.ThirdPartyFeignService;
import xyz.zerxoi.diomall.auth.vo.UserLoginVo;
import xyz.zerxoi.diomall.auth.vo.UserRegisterVo;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static xyz.zerxoi.common.constant.AuthConstant.SMS_CODE_CACHE_PREFIX;

@Data
@Controller
public class LoginController {
    private final ThirdPartyFeignService thirdPartyFeignService;
    private final StringRedisTemplate redisTemplate;
    private final MemberFeignService memberFeignService;

    public LoginController(ThirdPartyFeignService thirdPartyFeignService, StringRedisTemplate redisTemplate,
                           MemberFeignService memberFeignService) {
        this.thirdPartyFeignService = thirdPartyFeignService;
        this.redisTemplate = redisTemplate;
        this.memberFeignService = memberFeignService;
    }

    // 刷新时间和过期时间分离
    @ResponseBody
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
        redisTemplate.opsForValue().set(SMS_CODE_CACHE_PREFIX + phoneNum, code + ":" + System.currentTimeMillis(), 5,
                TimeUnit.MINUTES);

        thirdPartyFeignService.sendSms(phoneNum, code);
        return R.ok();
    }

    // 刷新时间和过期时间一致
    @ResponseBody
    @GetMapping("sendCodeV2")
    public R sendCodeV2(String phoneNum) {
        String code = UUID.randomUUID().toString().substring(0, 6);
        // 过期后刷新
        Boolean absent = redisTemplate.opsForValue().setIfAbsent(SMS_CODE_CACHE_PREFIX + phoneNum, code, 5,
                TimeUnit.MINUTES);
        if (Boolean.TRUE.equals(absent)) {
            thirdPartyFeignService.sendSms(phoneNum, code);
            return R.ok();
        }
        return R.error("验证码获取过于频繁");
    }

    // code 校验应该在登陆中校验
    @ResponseBody
    @GetMapping("validCode")
    public R validCode(String phoneNum, String code) {
        String codeTimestamp = redisTemplate.opsForValue().get(SMS_CODE_CACHE_PREFIX + phoneNum);
        if (StringUtils.isNotEmpty(codeTimestamp) && codeTimestamp.split(":")[0].equals(code)) {
            redisTemplate.delete(SMS_CODE_CACHE_PREFIX + phoneNum);
            return R.ok("验证码校验通过");
        }
        return R.error("验证码校验失败");
    }

    // code 校验应该在登录和注册中校验
    @ResponseBody
    @GetMapping("validCodeV2")
    public R validCodeV2(String phoneNum, String code) {
        String cache = redisTemplate.opsForValue().get(SMS_CODE_CACHE_PREFIX + phoneNum);
        if (code.equals(cache)) {
            redisTemplate.delete(SMS_CODE_CACHE_PREFIX + phoneNum);
            return R.ok("验证码校验通过");
        }
        return R.error("验证码校验失败");
    }

    @PostMapping("/login")
    public String login(UserLoginVo userLoginVo, HttpSession session) {
        // 使用Member服务进行登录
        R login = memberFeignService.login(userLoginVo);
        // 如果登录成功将MemberVo保存至Session（session.setAttribute(LOGIN_USER, memberVo)），并跳转到主页
        if (login.getCode() == 0) {
            String jsonString = JSON.toJSONString(login.get("data"));
            MemberVo memberVo = JSON.parseObject(jsonString, new TypeReference<>() {
            });
            session.setAttribute(AuthConstant.LOGIN_USER, memberVo);
            return "redirect:http://diomall.com";
        }
        // 如果登录失败显示失败消息，跳转到登陆界面
        return "redirect:http://auth.diomall.com/login.html";
    }

    @PostMapping("/register")
    public String register(@Valid UserRegisterVo userRegisterVo, BindingResult bindingResult,
                           RedirectAttributes redirectAttributes/*, Model model */) {
        if (bindingResult.hasErrors()) {
            // 转发使用 model
            // model.addAttribute("errors", bindingResult.getFieldErrors().stream()
            //         .filter(e -> e.getDefaultMessage() != null)
            //         .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (o1, o2) -> o1)));
            // 重定向使用 redirectAttribute
            // 重定向写大数据是利用session原理，将数据放在session中，只要跳到下个页面取出数据以后就会被删除
            redirectAttributes.addFlashAttribute("errors", bindingResult.getFieldErrors().stream()
                    .filter(e -> e.getDefaultMessage() != null)
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (o1, o2) -> o1)));
            return "redirect:http://auth.diomall.com/register.html";
        }
        String codeTimestamp = redisTemplate.opsForValue().get(SMS_CODE_CACHE_PREFIX + userRegisterVo.getPhone());
        if (StringUtils.isNotEmpty(codeTimestamp) && codeTimestamp.split(":")[0].equals(userRegisterVo.getCode())) {
            redisTemplate.delete(SMS_CODE_CACHE_PREFIX + userRegisterVo.getPhone());
        } else {
            Map<String,String > errors = new HashMap<>();
            errors.put("code", "验证码错误");
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.diomall.com/register.html";
        }

        R register = memberFeignService.register(userRegisterVo);
        if (register.getCode() == 0) {
            return "redirect:http://auth.diomall.com/login.html";
        } else {
            redirectAttributes.addFlashAttribute("msg", register.getMsg());
            return "redirect:http://auth.diomall.com/register.html";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute(AuthConstant.LOGIN_USER);
        return "redirect:http://auth.diomall.com/login.html";
    }
}
