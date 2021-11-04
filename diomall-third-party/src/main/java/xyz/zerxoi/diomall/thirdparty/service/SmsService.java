package xyz.zerxoi.diomall.thirdparty.service;

import lombok.Data;
import org.apache.http.HttpResponse;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import xyz.zerxoi.diomall.thirdparty.utils.HttpUtils;

import java.util.HashMap;
import java.util.Map;

@Data
@Service
@ConfigurationProperties("spring.cloud.alicloud.sms")
public class SmsService {
    private String host;
    private String path;
    private String method;
    private String appcode;
    private Integer expireAt;
    private String templateId;

    public void sendSms(String phoneNumber, String code) throws Exception {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE " + appcode);
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> queries = new HashMap<>();
        Map<String, String> bodies = new HashMap<>();
        bodies.put("content", "code:" + code + ",expire_at:" + expireAt);
        bodies.put("phone_number", phoneNumber);
        bodies.put("template_id", templateId);

        HttpUtils.doPost(host, path, method, headers, queries, bodies);
    }
}