// 文件路径：src\main\java\com\example\demo\controller\HomeController.java

package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// @Controller 注解的作用是：
//   使 Spring 将 被注解类 的 实例 作为 Spring 应用上下文中的 bean
@Controller
public class HomeController {
    // @GetMapping 注解的作用是：
    //   使 Spring MVC 将 被注解方法 作为处理 指定路由 的 GET请求 的处理器
    @GetMapping("/")
    public String home() {
        return "home";  // 返回视图名称
                        // 注：
                        //   如果使用 Thymeleaf，那么这个名称会传递给 Thymeleaf，
                        //   然后 Thymeleaf 据此从视图模板目录下找到 此名称.html 的视图模板，
                        //   最后 Thymeleaf 据此视图模板渲染形成视图页面后将其返回。
                        // 注：
                        //   默认的视图模板目录为 /src/main/resource/templates。
    }
}
