package com.example.demo.domain;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;

import javax.persistence.Entity;
import javax.persistence.Id;
// 如果使用 Spring Data JPA 那么需要用上面两个注解代替 Spring Data JDBC 的下面两个注解
// import org.springframework.data.relational.core.mapping.Table;
// import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
/*@Table*/  // 这个注解的作用是在数据库中创建一个对应的表，表名默认基于类名（Cat）生成，也可以指定，例如：@Table("Cat_Table")。
            // 注：当不指定表名时，表名默认为类名，但类名中的非首位大写字母会被转换为前缀下划线的形式，例如：Cat -> Cat，CatOrder -> Cat_Order。
            //     当不指定表名时，与不加 @Table 注解等效。也就是说，如果使用默认规则生成的表名，那么这个注解可以省略，Spring Data 也会自动创建这个表。
@Entity  // 使用 @Entity 代替 @Table 注解，以获得更多实体特性（包括实体映射、实体关系、实体生命周期）
public class Cat {

    @Id  // 这个注解的作用是声明 id 属性为数据库相应表（Cat表）中的主键
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;  // id 属性作为数据库表中的主键即 Cat 对象的唯一标识

    // 其它属性也会自动映射到数据库表中相应的字段，例如：name、createdAt、ingredients 等。
    // 也可以显式地使用 @Column 注解来指定属性与数据库表中字段的映射关系，例如：
    //     @Column("cat_name")
    //     // ...其它注解
    //     private String name;

    private Date createdAt = new Date();  // createdAt 属性用于存储 Cat 对象的创建时间
    
    @NotNull  // 校验以确保 name 属性的值不能为空
    @Size(min=3, message="Name must be at least 3 characters long")  // 校验以确保 name 属性的值至少包含 5 个字符
                                                                     // （否则会提示 message 属性中的消息）
    private String name;
    
    @NotNull  // 校验以确保 ingredients 属性的值不能为空
    @Size(min=1, message="You must choose at least 1 ingredient")  // 校验以确保 ingredients 属性的值（列表）至少包含 1 个元素
                                                                   // （否则会提示 message 属性中的消息）
    
    @ManyToMany()  // @ManyToMany 注解声明了这个实体类（Cat）与另一个实体类（Ingredient）建立的数据表上的多对多关系，
                   // 也就是 Cat 表与 Ingredient 表之间通过 某个中间表（这里默认是 Cat_Ingredient 表） 建立的多对多关系。
                   // 多对多的双方都可以是另一方的“拥有者”（owner），也可以是另一方的“被拥有者”（owned，即为另一方所拥有）。
                   // @ManyToMany 注解在一端实体类（Cat）的某个属性（ingredients）时，这一端即是“拥有者”（owner），
                   //   - 作为另一端（Ingredient）的*拥有者*，实体类（Cat）实例所拥有的另一实体类（Ingredient）实例的集合（ingredients）会被放入 ingredients 属性中。
                   //   - 作为另一端（Ingredient）的*拥有者*，实体类（Cat）上还可注解 @JoinTable 注解来指定：
                   //       - 中间表的名称（默认是 Cat_Ingredient）、
                   //       - 主表在中间表中的外键名称（默认是 cat_id）、
                   //       - 从表在中间表中的外键名称（默认是 ingredient_id）。
                   //     例如：
                   //         @ManyToMany
                   //         @JoinTable(
                   //             name="Cat_Ingredient",
                   //             joinColumns=@JoinColumn(name="cat_id"),
                   //             inverseJoinColumns=@JoinColumn(name="ingredient_id")
                   //         )
                   //         private List<Ingredient> ingredients;
                   //   - 作为*被拥有者*，实体类（Ingredient）上也可（但非必须）注解 @ManyToMany 在其某个属性（cats）上，从而成为另一端（Cat）的“拥有者”（owner）。
                   //     此时：
                   //       - 实体类（Ingredient）实例所拥有的另一实体类（Cat）实例的集合（cats）会被放入 cats 属性中。
                   //       - 实体类（Ingredient）上无需重复注解 @JoinTable 配置相同的中间表信息，只需注解 @ManyToMany 并指定 mappedBy 属性即可。
                   //         例如：@ManyToMany(mappedBy="<拥有者 @ManyToMany 所注解的属性名称（ingredients）>") 
    @JoinTable(
        name="Cat_Ingredient",
        joinColumns=@JoinColumn(name="cat_id"),
        inverseJoinColumns=@JoinColumn(name="ingredient_id")
    )
    private List<Ingredient> ingredients;

}
