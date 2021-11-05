package xyz.zerxoi.diomall.thirdparty.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.zerxoi.common.utils.R;
import xyz.zerxoi.diomall.thirdparty.service.SmsService;

@RestController
@RequestMapping("sms")
public class SmsController {

    private final SmsService smsService;

    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }

    @GetMapping("/sendSms")
    public R sendSms(@RequestParam("phoneNum") String phoneNum,@RequestParam("code") String code) throws Exception {
        smsService.sendSms(phoneNum, code);
        return R.ok();
    }

}
