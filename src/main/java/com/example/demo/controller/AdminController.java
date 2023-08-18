package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.service.OrderAdminService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private OrderAdminService orderAdminService;

    public AdminController(OrderAdminService orderAdminService) {
        this.orderAdminService = orderAdminService;
    }

    @PostMapping("/deleteAllOrder")
    public String deleteAllOrder() {
        orderAdminService.deleteAllOrder();
        return "redirect:/admin";
    }

}
