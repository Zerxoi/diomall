package xyz.zerxoi.diomall.order.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import xyz.zerxoi.diomall.order.interceptor.LoginInterceptor;

// 添加拦截器
@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    private final LoginInterceptor loginInterceptor;

    public WebConfiguration(LoginInterceptor loginInterceptor) {
        this.loginInterceptor = loginInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**");
    }
}
