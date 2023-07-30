package com.example.demo.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import javax.validation.Valid;
import org.springframework.validation.Errors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.extern.slf4j.Slf4j;
import com.example.demo.domain.Ingredient;
import com.example.demo.domain.Cat;
import com.example.demo.domain.CatOrder;
import com.example.demo.repository.IngredientRepository;

@Slf4j  // 这个注解的作用是在编译时自动生成一个 SLF4J 的 logger 作为被注解类的静态属性，
        // 且 logger 的名称为 "DesignCatController"，这个名称是根据被其注解类的名称推断出来的。
        // 等效于 private static final Logger log = LoggerFactory.getLogger(DesignCatController.class);
@Controller
@RequestMapping("/design")  // 这个注解的作用是将类中所有的处理器方法映射到 /design 路径下
                            // 然后再配合 @GetMapping 注解，注解到 showDesignForm() 方法，
                            // 就可以将 /design 路径下的 GET 请求映射给 showDesignForm() 方法处理。
@SessionAttributes("catOrder")  // 这个注解的作用是将 catOrder 对象保存到 session 中，
                                // 以便同一次业务跨不同请求（例如下单和支付等）使用这个 catOrder 对象。
public class DesignCatController {

    private final IngredientRepository ingredientRepository;

    // @Autowired  // 这里无需使用 @Autowired 注解来注入 IngredientRepository 对象，
                   // 因为此处 DesignCatController 类只有一个构造器，其参数为 IngredientRepository 对象，
                   // 这种情况下，Spring 会自动将 IngredientRepository 对象注入到构造器中。
    public DesignCatController(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    /**
     * 这个方法的作用是将所有的 Ingredient 对象添加到模型中，
     * 目的是为了在 Thymeleaf 中使用 th:object="${session.catOrder}" 来访问 catOrder 对象。
     * @param model 模型对象
     */
    @ModelAttribute  // 这里使用 @ModelAttribute 注解的作用是将 addIngredientsToModel() 方法的返回值添加到数据模型 model 中，
                     // 并且当没有指定 name 属性时，它会使用方法返回值的类型的小写字符串作为要添加的值在 model 对象中的属性名。
                     // 它会在所有的处理器方法执行之前执行。
    public void addIngredientsToModel(Model model) {
        
        // 代替原来的硬编码方式，从数据库中获取 Ingredient 对象列表
        List<Ingredient> ingredients = ingredientRepository.findAll();
        // // 在引入数据库之前，先使用硬编码的方式来创建 Ingredient 对象列表
        // List<Ingredient> ingredients = Arrays.asList(
        //     new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP),
        //     new Ingredient("COTO", "Corn Tortilla", Ingredient.Type.WRAP),
        //     new Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN),
        //     new Ingredient("CARN", "Carnitas", Ingredient.Type.PROTEIN),
        //     new Ingredient("TMTO", "Diced Tomatoes", Ingredient.Type.VEGGIES),
        //     new Ingredient("LETC", "Lettuce", Ingredient.Type.VEGGIES),
        //     new Ingredient("CHED", "Cheddar", Ingredient.Type.CHEESE),
        //     new Ingredient("JACK", "Monterrey Jack", Ingredient.Type.CHEESE),
        //     new Ingredient("SLSA", "Salsa", Ingredient.Type.SAUCE),
        //     new Ingredient("SRCR", "Sour Cream", Ingredient.Type.SAUCE)
        // );

        // 在得到 Ingredient 对象列表后，将其格式化成 Map映射 后添加到数据模型中
        // 先初始化 ingredientsMap 对象并添加到数据模型中
        Map<String, List<Ingredient>> ingredientsMap = new HashMap<>();  
        model.addAttribute("ingredientsMap", ingredientsMap);
        Ingredient.Type[] ingredientTypes = Ingredient.Type.values();
        for (Ingredient.Type ingredientType : ingredientTypes) {
            // 将 Ingredient.Type 值转换为小写字符串，然后作为模型的属性名，
            // 将 filterIngredientsByType() 方法返回的 Ingredient 对象列表作为模型的属性值，
            // 从而构造出一个 配料类型 到 属于该配料类型的具体配料列表 的映射，并添加到 ingredientsMap。
            ingredientsMap.put(
                ingredientType.toString().toLowerCase(), 
                filterIngredientsByType(ingredients, ingredientType)
            );
        }
    }

    @ModelAttribute(name = "catOrder")  // 这个注解的作用是将 catOrder 对象添加到 数据模型 model 中，
                                        // 它会在所有的处理器方法执行之前执行。
    public CatOrder catOrder() {
        return new CatOrder();
    }

    @ModelAttribute(name = "cat")  // 这个注解的作用是将 cat 对象添加到 数据模型 model 中，
                                   // 它会在所有的处理器方法执行之前执行。
    public Cat cat() {
        return new Cat();
    }

    @GetMapping
    public String showDesignForm(Model model) {
        log.info("喵喵喵，Designing cat");
        addIngredientsToModel(model);  // 向模型中添加 Ingredient 对象列表
        return "design";  // 返回视图名，由模板引擎解析为具体视图
    }

    @PostMapping
    public String processDesign(
        @Valid Cat cat, Errors errors,
        @ModelAttribute("catOrder") CatOrder catOrder  // 这个 <注解(在方法参数上)> 的作用是 将 <model 中的 catOrder 对象> 绑定至 <该方法参数>，
                                                       // 其中，<model 中的 catOrder 对象> 是 @ModelAttribute(name = "catOrder") 注解在 <本类的catOrder属性> 上添加的。
                                                       // 即这段代码：
                                                       //     @ModelAttribute(name = "catOrder")
                                                       //     public CatOrder catOrder() {
                                                       //         return new CatOrder();
                                                       //     }
    ) {
        log.info("喵喵喵，Processing design: " + cat);

        if (errors.hasErrors()) {
            log.info("喵喵喵，Error Processing design: " + cat + ", errors: " + errors);
            return "design";
        }

        catOrder.addDesign(cat);
        return "redirect:/orders/current";
    }

    private List<Ingredient> filterIngredientsByType(List<Ingredient> ingredients, Ingredient.Type type) {
        return ingredients
            .stream()  // 将 ingredients 转换为流，以便使用流操作
            .filter(x -> x.getType().equals(type))  // 过滤出类型为 type 的 Ingredient 对象
            .collect(Collectors.toList());  // 将过滤出的 Ingredient 对象收集到列表中
    }

}
