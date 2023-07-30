package com.example.demo.converter;

import java.util.HashMap;
import java.util.Map;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.example.demo.domain.Ingredient;
import com.example.demo.repository.IngredientRepository;

@Component  // 这个注解的作用是将 IngredientByIdConverter 类声明为 Spring 组件，
            // 这样 Spring 就会自动将其创建为 Spring 应用上下文中的一个 bean，
            // 然后在需要使用 IngredientByIdConverter 类型的 bean 的地方，就可以直接注入这个 bean。
            // 在 Spring MVC 框架中，
            //     会自动注入这个 bean 到请求处理过程（在请求到达处理器之前），
            //     接收到请求提交数据后会自动调用这个 bean 的 convert() 方法，
            //     然后将提交数据转换后的值注入到相应处理器方法的参数中。
public class IngredientByIdConverter implements Converter<String, Ingredient> {

    private final IngredientRepository ingredientRepository;

    // @Autowired  // 这里无需使用 @Autowired 注解来注入 IngredientRepository 对象，
                   // 因为此处 DesignCatController 类只有一个构造器，其参数为 IngredientRepository 对象，
                   // 这种情况下，Spring 会自动将 IngredientRepository 对象注入到构造器中。
    public IngredientByIdConverter(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
        // 代替原来的硬编码方式，改为从数据库中获取指定 id 的 Ingredient 对象列表的方式来实现 converter
        // 因此，这里不再需要 ingredientsMap 了
        // // 在引入数据库之前，先使用硬编码的方式来创构造 ingredientsMap 即 Ingredient id 到 Ingredient 对象的映射
        // ingredientsMap.put("FLTO", new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP));
        // ingredientsMap.put("COTO", new Ingredient("COTO", "Corn Tortilla", Ingredient.Type.WRAP));
        // ingredientsMap.put("GRBF", new Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN));
        // ingredientsMap.put("CARN", new Ingredient("CARN", "Carnitas", Ingredient.Type.PROTEIN));
        // ingredientsMap.put("TMTO", new Ingredient("TMTO", "Diced Tomatoes", Ingredient.Type.VEGGIES));
        // ingredientsMap.put("LETC", new Ingredient("LETC", "Lettuce", Ingredient.Type.VEGGIES));
        // ingredientsMap.put("CHED", new Ingredient("CHED", "Cheddar", Ingredient.Type.CHEESE));
        // ingredientsMap.put("JACK", new Ingredient("JACK", "Monterrey Jack", Ingredient.Type.CHEESE));
        // ingredientsMap.put("SLSA", new Ingredient("SLSA", "Salsa", Ingredient.Type.SAUCE));
        // ingredientsMap.put("SRCR", new Ingredient("SRCR", "Sour Cream", Ingredient.Type.SAUCE));
    }

    @Override
    public Ingredient convert(String id) {
        // 代替原来的从 ingredientsMap 中获取 Ingredient 对象的方式，
        // 改为从数据库中获取指定 id 的 Ingredient 对象列表的方式来实现 converter
        return ingredientRepository.findById(id).orElse(null);  // 注意：orElse(null) 是一种过于简单的处理方式，不推荐。
                                                                      //       因为它会导致空指针异常（NullPointerException），
                                                                      //       从而抵消了使用 Optional 的好处。
                                                                      //       如果不确定应该返回什么值时，可以使用 orElseThrow() 方法来抛出一个异常。
        // // 实现从 id 到 Ingredient 对象的转换
        // return ingredientsMap.get(id);
    }

}
