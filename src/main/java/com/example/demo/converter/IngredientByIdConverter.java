package com.example.demo.converter;

import java.util.HashMap;
import java.util.Map;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.example.demo.domain.Ingredient;

@Component  // 这个注解的作用是将 IngredientByIdConverter 类声明为 Spring 组件，
            // 这样 Spring 就会自动将其创建为 Spring 应用上下文中的一个 bean，
            // 然后在需要使用 IngredientByIdConverter 类型的 bean 的地方，就可以直接注入这个 bean。
            // 在 Spring MVC 框架中，
            //     会自动注入这个 bean 到请求处理过程（在请求到达处理器之前），
            //     接收到请求提交数据后会自动调用这个 bean 的 convert() 方法，
            //     然后将提交数据转换后的值注入到相应处理器方法的参数中。
public class IngredientByIdConverter implements Converter<String, Ingredient> {

    // ingredientsMap 用于保存 Ingredient id 到 Ingredient 对象的映射
    // 从而在 convert() 方法中可以根据 Ingredient id 来获取对应的 Ingredient 对象
    private Map<String, Ingredient> ingredientsMap = new HashMap<>();

    public IngredientByIdConverter() {
        // 在引入数据库之前，先使用硬编码的方式来创构造 ingredientsMap 即 Ingredient id 到 Ingredient 对象的映射
        ingredientsMap.put("FLTO", new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP));
        ingredientsMap.put("COTO", new Ingredient("COTO", "Corn Tortilla", Ingredient.Type.WRAP));
        ingredientsMap.put("GRBF", new Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN));
        ingredientsMap.put("CARN", new Ingredient("CARN", "Carnitas", Ingredient.Type.PROTEIN));
        ingredientsMap.put("TMTO", new Ingredient("TMTO", "Diced Tomatoes", Ingredient.Type.VEGGIES));
        ingredientsMap.put("LETC", new Ingredient("LETC", "Lettuce", Ingredient.Type.VEGGIES));
        ingredientsMap.put("CHED", new Ingredient("CHED", "Cheddar", Ingredient.Type.CHEESE));
        ingredientsMap.put("JACK", new Ingredient("JACK", "Monterrey Jack", Ingredient.Type.CHEESE));
        ingredientsMap.put("SLSA", new Ingredient("SLSA", "Salsa", Ingredient.Type.SAUCE));
        ingredientsMap.put("SRCR", new Ingredient("SRCR", "Sour Cream", Ingredient.Type.SAUCE));
    }

    @Override
    public Ingredient convert(String id) {
        // 实现从 id 到 Ingredient 对象的转换
        return ingredientsMap.get(id);
    }

}
