package xyz.zerxoi.diomall.auth.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class TemplateController {
    @GetMapping("/{template}.html")
    public String templateHtml(@PathVariable("template") String template) {
        return template;
    }
}
