package com.example.demo;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.domain.Ingredient;
import com.example.demo.repository.CatRepository;
import com.example.demo.repository.IngredientRepository;
import com.example.demo.repository.UserRepository;

import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

import com.example.demo.domain.Cat;

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

    @Bean
    public CommandLineRunner dataLoader(
        IngredientRepository ingredentRepo,
        UserRepository userRepo,
        PasswordEncoder encoder,
        CatRepository catRepo
    ) {
        return args -> {

            // 初始化时，模拟一些数据

            // 先模拟一些 Ingredient 数据

            Ingredient flourTortilla = new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP);
            Ingredient cornTortilla = new Ingredient("COTO", "Corn Tortilla", Ingredient.Type.WRAP);
            Ingredient groundBeef = new Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN);
            Ingredient carnitas = new Ingredient("CARN", "Carnitas", Ingredient.Type.PROTEIN);
            Ingredient tomatoes = new Ingredient("TMTO", "Diced Tomatoes", Ingredient.Type.VEGGIES);
            Ingredient lettuce = new Ingredient("LETC", "Lettuce", Ingredient.Type.VEGGIES);
            Ingredient cheddar = new Ingredient("CHED", "Cheddar", Ingredient.Type.CHEESE);
            Ingredient jack = new Ingredient("JACK", "Monterrey Jack", Ingredient.Type.CHEESE);
            Ingredient salsa = new Ingredient("SLSA", "Salsa", Ingredient.Type.SAUCE);
            Ingredient sourCream = new Ingredient("SRCR", "Sour Cream", Ingredient.Type.SAUCE);
            
            ingredentRepo.save(flourTortilla);
            ingredentRepo.save(cornTortilla);
            ingredentRepo.save(groundBeef);
            ingredentRepo.save(carnitas);
            ingredentRepo.save(tomatoes);
            ingredentRepo.save(lettuce);
            ingredentRepo.save(cheddar);
            ingredentRepo.save(jack);
            ingredentRepo.save(salsa);
            ingredentRepo.save(sourCream);

            // 再模拟一些 Cat 数据

            Cat cat1 = new Cat();
            cat1.setName("Garfield");
            cat1.setIngredients(Arrays.asList(
                flourTortilla, groundBeef, carnitas, sourCream, salsa, cheddar
            ));
            catRepo.save(cat1);

            Cat cat2 = new Cat();
            cat2.setName("Sylvester");
            cat2.setIngredients(Arrays.asList(
                cornTortilla, groundBeef, cheddar, jack, sourCream 
            ));
            catRepo.save(cat2);

            Cat cat3 = new Cat();
            cat3.setName("Tom");
            cat3.setIngredients(Arrays.asList(
                flourTortilla, cornTortilla, tomatoes, lettuce, salsa
            ));
            catRepo.save(cat3);
        };
    }

}
