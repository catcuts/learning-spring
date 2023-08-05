package com.example.demo.controller;

import javax.validation.Valid;
import org.springframework.validation.Errors;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import lombok.extern.slf4j.Slf4j;
import com.example.demo.domain.CatOrder;
import com.example.demo.repository.CatOrderRepository;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("catOrder")
public class OrderController {

    private CatOrderRepository catOrderRepository;

    public OrderController(CatOrderRepository catOrderRepository) {
        this.catOrderRepository = catOrderRepository;
    }
    
    @GetMapping("/current")
    public String orderForm() {
        log.info("喵喵喵，Checking order");
        return "orderForm";
    }

    @PostMapping
    public String processOrder(
        @Valid CatOrder catOrder, Errors errors, 
        SessionStatus sessionStatus
    ) {
        log.info("喵喵喵，Processing order: " + catOrder);

        if (errors.hasErrors()) {
            log.info("喵喵喵，Error Processing order: " + catOrder + ", errors: " + errors);
            return "orderForm";
        }

        catOrderRepository.save(catOrder);

        sessionStatus.setComplete();
        return "redirect:/";
    }

}