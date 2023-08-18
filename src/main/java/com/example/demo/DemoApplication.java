package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.demo.repository")
@EntityScan(basePackages = "com.example.demo.domain")
public class DemoApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	// 注：
	//    WebMvcConfigurer 是一个接口，它定义了一些方法，
	//    这些方法可以用来自定义 Spring MVC 的配置。
	//    例如自定义视图控制器（addViewControllers），
	//    以下是自定义视图控制器的示例：

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		// 将 /anotherhome 请求映射到 home 视图，
		// 这种方式适用于不需要进行业务处理，只需要返回一个页面的情况，
		// 在这种情况下，不需要自己创建一个控制器类，只需如下将路由与视图绑定即可。
		registry.addViewController("/anotherhome").setViewName("home");
        // 将 /login 请求映射到 login 视图，同理。
        registry.addViewController("/login")/*.setViewName("login")  // 如果视图名与请求路径相同，那么可以省略 setViewName() 方法*/;
        // 将 /admin 请求映射到 admin 视图，同理。
        registry.addViewController("/admin")/*可省略.setViewName("admin")*/;
	}

}
