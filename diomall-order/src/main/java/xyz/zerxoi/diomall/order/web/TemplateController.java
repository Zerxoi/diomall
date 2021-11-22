package xyz.zerxoi.diomall.order.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import xyz.zerxoi.common.constant.AuthConstant;

import javax.servlet.http.HttpSession;

@Controller
public class TemplateController {
    @GetMapping("/{template}.html")
    public String templateHtml(@PathVariable("template") String template) {
        return template;
    }
}
