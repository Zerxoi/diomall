package xyz.zerxoi.diomall.auth.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class UserRegisterVo {
    @NotEmpty
    @Length(min = 6, max = 18, message = "用户名必须是6-18个字符")
    private String username;
    @NotEmpty
    @Length(min = 6, max = 18, message = "密码必须是6-18个字符")
    private String password;
    @Pattern(regexp = "^1[3456789]\\d{9}$", message = "手机号码格式不正确")
    private String phone;
    @NotEmpty(message = "验证码不能为空")
    private String code;
}
