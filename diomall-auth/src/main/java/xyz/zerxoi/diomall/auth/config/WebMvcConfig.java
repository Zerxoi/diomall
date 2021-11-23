package xyz.zerxoi.diomall.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    // 与 TemplateController 二选一
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("login.html").setViewName("login");
        registry.addViewController("register.html").setViewName("register");
    }
}
