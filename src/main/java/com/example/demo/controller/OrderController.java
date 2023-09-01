package com.example.demo.controller;

import javax.validation.Valid;
import java.security.Principal;
import org.springframework.validation.Errors;
import org.springframework.stereotype.Controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PutMapping(path = "/{id}", consumes = "application/json")  // 对于 PUT 请求，并且请求内容类型是 application/json
                                                                // 注：PUT 请求会替换整个资源，而不是部分更新，如果请求提交的数据中缺少某个属性，则该属性会被置为 null。
    public CatOrder putOrder(@PathVariable("id") Long id, @RequestBody CatOrder catOrder) {
        // 注：@PathVariable 注解表示将请求路径中的 id 参数绑定到方法的 id 参数上。
        //     @RequestBody 注解表示请求体 body 中的内容会被反序列化为 catOrder 对象，从而可以通过 catOrder 对象获取请求体中的内容。
        if (catOrder.getId() != id) {
            // 如果请求体中的 id 与请求路径中的 id 不一致，则返回 400 状态码，表示请求无效。
            throw new IllegalStateException("Given cat's id doesn't match the id in the path.");
        }
        return catOrderRepository.save(catOrder);
    }

    @PatchMapping(path = "/{id}", consumes = "application/json")  // 对于 PATCH 请求，并且请求内容类型是 application/json
                                                                  // 注：PATCH 请求会部分更新资源，如果请求提交的数据中缺少某个属性，则该属性不会被更新。
    public CatOrder patchOrder(@PathVariable("id") Long id, @RequestBody CatOrder catOrder) {
        // 注：@PathVariable 注解表示将请求路径中的 id 参数绑定到方法的 id 参数上。
        //     @RequestBody 注解表示请求体 body 中的内容会被反序列化为 catOrder 对象，从而可以通过 catOrder 对象获取请求体中的内容。
        if (catOrder.getId() != id) {
            // 如果请求体中的 id 与请求路径中的 id 不一致，则返回 400 状态码，表示请求无效。
            throw new IllegalStateException("Given cat's id doesn't match the id in the path.");
        }
        CatOrder oldCatOrder = catOrderRepository.findById(id).get();
        if (catOrder.getDeliveryName() != null) {
            oldCatOrder.setDeliveryName(catOrder.getDeliveryName());
        }
        if (catOrder.getDeliveryStreet() != null) {
            oldCatOrder.setDeliveryStreet(catOrder.getDeliveryStreet());
        }
        if (catOrder.getDeliveryCity() != null) {
            oldCatOrder.setDeliveryCity(catOrder.getDeliveryCity());
        }
        if (catOrder.getDeliveryState() != null) {
            oldCatOrder.setDeliveryState(catOrder.getDeliveryState());
        }
        if (catOrder.getDeliveryZip() != null) {
            oldCatOrder.setDeliveryZip(catOrder.getDeliveryZip());
        }
        if (catOrder.getCcNumber() != null) {
            oldCatOrder.setCcNumber(catOrder.getCcNumber());
        }
        if (catOrder.getCcExpiration() != null) {
            oldCatOrder.setCcExpiration(catOrder.getCcExpiration());
        }
        if (catOrder.getCcCVV() != null) {
            oldCatOrder.setCcCVV(catOrder.getCcCVV());
        }
        return catOrderRepository.save(oldCatOrder);
    }

    @DeleteMapping("/{id}")  // 对于 DELETE 请求
    public ResponseEntity<CatOrder> deleteOrder(@PathVariable("id") Long id) {
        // 注：@PathVariable 注解表示将请求路径中的 id 参数绑定到方法的 id 参数上。
        CatOrder catOrder = catOrderRepository.findById(id).get();
        // 如果找不到 CatOrder 对象，则返回 404 状态码，表示资源不存在。
        if (catOrder == null) return new ResponseEntity<>(null, HttpStatus.NOT_FOUND/*404*/);
        // 如果找到了 CatOrder 对象，则删除它，并返回 204 状态码，表示请求成功但没有响应内容。
        catOrderRepository.delete(catOrder);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT/*204*/);
    }

}
