package xyz.zerxoi.diomall.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.zerxoi.diomall.auth.component.SmsComponent;

@SpringBootTest
class DiomallAuthApplicationTests {

    @Autowired
    private SmsComponent smsComponent;

    @Test
    void sendSmsTest() {
        smsComponent.sendSms("19818965442", "123145");
    }

}
