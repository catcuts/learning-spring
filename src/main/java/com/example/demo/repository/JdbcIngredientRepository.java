package com.example.demo.repository;

/*
import java.util.List;
import java.util.Optional;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Ingredient;

**
 * 注：这是一个 IngredientRepository 的实现类，
 *     它基于 JdbcTemplate 技术来实现 IngredientRepository 接口中定义的方法。
 *
@Repository
public class JdbcIngredientRepository implements IngredientRepository {

    private JdbcTemplate jdbcTemplate;

    public JdbcIngredientRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Ingredient> findAll() {
        // 注：
        //    jdbcTemplate.query() 方法接收三个参数：
        //      - 第一个参数是 SQL 语句；
        //      - 第二个参数是一个 RowMapper 实现，用于将查询结果的每一行（Row）映射为一个 Ingredient 对象；
        //      - 第三个参数是一个可变参数，用于指定 SQL 语句中的占位符的值。例如见 findById() 和 save()。
        return jdbcTemplate.query(
            "select id, name, type from Ingredient",
            this::mapRowToIngredient
        );
    }

    @Override
    public Optional<Ingredient> findById(String id) {
        List<Ingredient> results = jdbcTemplate.query(
            "select id, name, type from Ingredient where id=?",
            this::mapRowToIngredient,  // 这里使用了 Java 8 的方法引用（method reference）语法和 Lambda 表达式语法
                                       // 用来替代显式地创建 RowMapper 类实例，从而简化代码。
            id  // 这里的 id 会替换 SQL 语句中的第一个占位符（?），此后的参数会依次替换 SQL 语句中对应位置的占位符（?）。
        );
        return results.size() == 0 ? 
            Optional.empty()  // 构造一个空的 Optional 对象
            : 
            Optional.of(results.get(0));  // 构造一个包含指定值的 Optional 对象
    }

    **
     * 注：
     *     这个 RowMapper 的实现没有直接 implements org.springframework.jdbc.core.RowMapper 接口，也是可行的。
     *     只要这个函数的返回值类型是 Ingredient（在此示例中），且它的参数列表是 ResultSet 和 int，那么它就是一个可用的 RowMapper 的实现。
     *
    private Ingredient mapRowToIngredient(ResultSet row, int rowNum) throws SQLException {
        return new Ingredient(
            row.getString("id"),
            row.getString("name"),
            Ingredient.Type.valueOf(row.getString("type"))
        );
    }   

    @Override
    public Ingredient save(Ingredient ingredient) {
        jdbcTemplate.update(
            "insert into Ingredient (id, name, type) values (?, ?, ?)",
            ingredient.getId(),
            ingredient.getName(),
            ingredient.getType().toString()
        );
        return ingredient;
    }

}
*/