package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import com.example.demo.domain.Ingredient;

/**
 * 注：这是一个 IngredientRepository 的接口，
 *     它定义了一些方法，用于操作数据库中的 Ingredient 对象，
 *     例如：
 *       - 查找所有的 Ingredient 对象、
 *       - 根据 id 查找 Ingredient 对象、
 *       - 保存 Ingredient 对象等。
 *     这个接口的实现类可以基于 JDBC、JdbcTemplate、JPA、MyBatis(Plus) 等技术或框架来实现。
 *     例如：JdbcIngredientRepository 类就是基于 JdbcTemplate 技术来实现的。
 */
public interface IngredientRepository {

    List<Ingredient> findAll();

    // 注：
    //    这里使用 Optional<Ingredient> 作为返回值类型，
    //    是为了避免返回 null 值，从而避免空指针异常（NullPointerException）。
    //    它会强制调用者在使用返回值之前，先检查返回值是否为空或者做防止返回空值的处理，否则编译器会报错。
    //    例如：
    //      - 调用者可以在返回值上：
    //        - 使用 orElse() 方法来在其为空时使用一个默认值；
    //        - 使用 orElseThrow() 方法来在其为空时抛出一个异常；
    //        - 使用 ifPresent() 方法来在其为非空时执行一个操作等。
    //      - 调用者也可以在 Optional 类上：
    //        - 使用 empty() 方法来创建一个空的 Optional 对象等。
    Optional<Ingredient> findById(String id);

    Ingredient save(Ingredient ingredient);

}
