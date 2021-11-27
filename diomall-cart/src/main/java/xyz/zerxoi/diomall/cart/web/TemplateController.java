package xyz.zerxoi.diomall.cart.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TemplateController {
    // 可以实现 WebMvcConfigurer 的 addViewControllers 方法
    @GetMapping("/{template}.html")
    public String templateHtml(@PathVariable("template") String template) {
        return template;
    }
}
