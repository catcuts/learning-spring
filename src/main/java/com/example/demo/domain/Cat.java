package com.example.demo.domain;

import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table  // 这个注解的作用是在数据库中创建一个对应的表，表名默认基于类名（Cat）生成，也可以指定，例如：@Table("Cat_Table")。
        // 注：当不指定表名时，表名默认为类名，但类名中的非首位大写字母会被转换为前缀下划线的形式，例如：Cat -> Cat，CatOrder -> Cat_Order。
        //     当不指定表名时，与不加 @Table 注解等效。也就是说，如果使用默认规则生成的表名，那么这个注解可以省略，Spring Data 也会自动创建这个表。
public class Cat {

    @Id  // 这个注解的作用是声明 id 属性为数据库相应表（Cat表）中的主键
    private Long id;  // id 属性作为数据库表中的主键即 Cat 对象的唯一标识

    // 其它属性也会自动映射到数据库表中相应的字段，例如：name、createdAt、ingredients 等。
    // 也可以显式地使用 @Column 注解来指定属性与数据库表中字段的映射关系，例如：
    //     @Column("cat_name")
    //     // ...其它注解
    //     private String name;

    private Date createdAt = new Date();  // createdAt 属性用于存储 Cat 对象的创建时间
    
    @NotNull  // 校验以确保 name 属性的值不能为空
    @Size(min=5, message="Name must be at least 5 characters long")  // 校验以确保 name 属性的值至少包含 5 个字符
                                                                     // （否则会提示 message 属性中的消息）
    private String name;
    
    @NotNull  // 校验以确保 ingredients 属性的值不能为空
    @Size(min=1, message="You must choose at least 1 ingredient")  // 校验以确保 ingredients 属性的值（列表）至少包含 1 个元素
                                                                   // （否则会提示 message 属性中的消息）
    private List<CatIngredient> ingredients;

}
