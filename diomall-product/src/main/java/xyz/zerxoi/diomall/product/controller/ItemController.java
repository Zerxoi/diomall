package xyz.zerxoi.diomall.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ItemController {
    @GetMapping("/item")
    public String templateHtml(@RequestParam Long skuId) {
        System.out.println(skuId);
        return "item";
    }
}
