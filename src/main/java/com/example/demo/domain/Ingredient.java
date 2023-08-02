package com.example.demo.domain;

import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

@Data
@Table
@AllArgsConstructor  // 这个注解的作用是生成一个包含所有属性的构造函数，例如：
                     //     public Ingredient(String id, String name, Type type) {
                     //         this.id = id;
                     //         this.name = name;
                     //         this.type = type;
                     //     }
                     // 这样就可以使用这个构造函数来创建 Ingredient 对象了（注：这不是出于 Spring Data 的需要）。
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)  // 这个注解的作用是 *确保* 这个类有一个无参构造函数，
                                                                // 因为 Spring Data 需要通过反射来创建这个类的实例，以便进行数据持久化。
                                                                // 同时这个注解设定了：
                                                                //    - access = AccessLevel.PRIVATE，
                                                                //      即设定无参构造函数的访问权限是 private（私有），
                                                                //      这样就避免被反射之外的其它方式代码调用（例如使用Class.newInstance()或Constructor.newInstance()等方法）。
                                                                //    - force = true，
                                                                //      即强制生成这个无参构造函数，即使这个类已经定义了其它的构造函数。
                                                                //      *确保*的关键在于这个 force = true。
                                                                // 为什么说是 *确保* 呢？因为：
                                                                //    - Java 会为每个类自动生成一个无参构造函数，但是只有当这个类没有定义其它构造函数时才会生成；
                                                                //    - 如果没有这个注解，那么当这个类已经定义了其它的构造函数时，Java 就不会为这个类自动生成无参构造函数了。
                                                                // 参考：书中 3.3.2 节 或 https://stackoverflow.com/questions/68314072/why-to-use-allargsconstructor-and-noargsconstructor-together-over-an-entity
                                                                // 注：虽然相关的参考或问答大都是针对 JPA 的，但不论是 Spring Data JDBC 还是 Spring Data JPA，其数据持久化过程都是需要通过反射来创建所需类实例的。
public class Ingredient implements Persistable<String> {
    // 注：
    
    @Id
    private final String id;
    private final String name;
    private final Type type;

    public static enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }

    @Override
    // @Transient  // 注：默认情况下这里无需使用 @Transient 注解，因为 Spring Data 默认的访问类型是 AccessType.FIELD，
                   //     也就是访问属性来持久化而不是访问 getter/setter，因此 new 不会被 Spring Data 访问并持久化到数据库
                   //     （如果要使用 getter/setter 的访问方式那么 Spring Data 的访问类型应设为 AccessType.PROPERTY）。
    public boolean isNew() {
        return true;  // 注：isNew() 实现是用于 Spring Data 在持久化之前调用它来判断对象是否是要新建的（is new），
                      //     一种实现方式是直接返回 true，这样 Spring Data 就会直接认为这个对象是新建的，从而在持久化时直接执行 insert 操作，否则执行 update 操作。
                      //     这种实现的原因是因为没有实现 getId() 方法，从而这个对象的 id 属性是由数据库自动生成的，因此在持久化之前是无法确定这个对象是否是新建的。
                      //     如果实现了 getId() 方法，从而这个对象的 id 属性是由应用程序生成的，那么就可以根据 id 属性是否为 null 来判断这个对象是否是新建的，
                      //     例如：
                      //         @Override
                      //         public boolean isNew() {
                      //             return this.id == null;
                      //         }
                      //     这样 Spring Data 就会根据 id 属性是否为 null 来判断这个对象是否是新建的，从而在持久化时执行 insert 或 update 操作。
    }

}
