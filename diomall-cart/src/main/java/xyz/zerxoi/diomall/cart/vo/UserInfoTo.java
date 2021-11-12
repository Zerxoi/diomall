package xyz.zerxoi.diomall.cart.vo;

import lombok.Data;

@Data
public class UserInfoTo {
    private Long userId;
    private String userKey;
    // 临时用户：登陆之前Cookie中是否含有 user-key
    private boolean userKeyInCookie;
}
