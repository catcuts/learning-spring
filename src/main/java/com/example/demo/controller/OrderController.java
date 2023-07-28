package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import lombok.extern.slf4j.Slf4j;
import com.example.demo.domain.CatOrder;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("catOrder")
public class OrderController {
    
    @GetMapping("/current")
    public String orderForm() {
        log.info("喵喵喵，Checking order");
        return "orderForm";
    }

    @PostMapping
    public String processOrder(
        CatOrder catOrder, 
        SessionStatus sessionStatus
    ) {
        log.info("喵喵喵，Processing order: " + catOrder);
        sessionStatus.setComplete();
        return "redirect:/";
    }

}
