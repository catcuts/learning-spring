package com.example.demo.controller;

import javax.validation.Valid;
import java.security.Principal;
import org.springframework.validation.Errors;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import lombok.extern.slf4j.Slf4j;
import com.example.demo.domain.CatOrder;
import com.example.demo.domain.User;
import com.example.demo.repository.CatOrderRepository;
import com.example.demo.repository.UserRepository;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("catOrder")
public class OrderController {

    private CatOrderRepository catOrderRepository;
    private UserRepository userRepository;

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
        SessionStatus sessionStatus,
        Principal principal,  // 这是一个 Java Security 提供的接口，用于获取当前登录的用户信息
                              // 直接使用这个接口的缺点是在安全无关的功能中引入了安全相关的接口，
                              // 这样会导致代码的耦合性增加。因此考虑替换成 Authentication 对象。
        Authentication authentication,  // 这是一个 Spring Security 提供的接口，也用于获取当前登录的用户信息
        @AuthenticationPrincipal User user  // 这是一个 Spring Security 提供的注解，用于获取当前登录的用户对象。
                                            // 使用它来获取当前登录的用户对象比使用 Authentication 对象更加简便，而且无需类型转换。
    ) {
        log.info("喵喵喵，Processing order: " + catOrder);

        if (errors.hasErrors()) {
            log.info("喵喵喵，Error Processing order: " + catOrder + ", errors: " + errors);
            return "orderForm";
        }

        catOrderRepository.save(catOrder);

        // 获取当前登录的用户对象（基于 Java Security）
        // User user = userRepository.findByUsername(principal.getName()/*当前登录的用户名*/);
        
        // 获取当前登录的用户对象（基于 Spring Security）
        // User user = (User) authentication.getPrincipal();  // 注：getPricipal() 方法返回的是一个 java.util.Object 对象，
                                                              // 需要强制转换成所需的 User 对象。
        
        // 注：
        //   1. 使用了 @AuthenticationPrincipal 注解后，就不需要再使用上面两种方式来获取当前登录的用户对象了，而是自动注入到本方法的 user 参数中。
        //   2. 还有一种方式是使用 SecurityContextHolder.getContext().getAuthentication().getPrincipal() 来获取当前登录的用户对象，
        //      这种方式需要强制转换（User user = (User) authentication.getPrincipal()），不如使用 @AuthenticationPrincipal 注解方便。
        //      但是这种方式可以在任何地方使用，而不仅仅是在 Controller 中，因此适合在较低级别的代码中使用。
        
        // 将当前用户对象关联到订单对象中
        catOrder.setUser(user);

        sessionStatus.setComplete();
        return "redirect:/";
    }

}
