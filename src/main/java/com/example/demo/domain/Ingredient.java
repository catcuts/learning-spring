package com.example.demo.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
// 如果使用 Spring Data JPA 那么需要用上面两个注解代替 Spring Data JDBC 的下面两个注解
// import org.springframework.data.relational.core.mapping.Table;
// import org.springframework.data.annotation.Id;

import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

@Data
/*@Table*/
@Entity  // 使用 @Entity 代替 @Table 注解，以获得更多实体特性（包括实体映射、实体关系、实体生命周期）
@AllArgsConstructor  // 这个注解的作用是生成一个包含所有属性的构造函数，例如：
                     //     public Ingredient(String id, String name, Type type) {
                     //         this.id = id;
                     //         this.name = name;
                     //         this.type = type;
                     //     }
                     // 这样就可以使用这个构造函数来创建 Ingredient 对象了（注：这不是出于 Spring Data 的需要）。
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)  // 这个注解的作用是 *确保* 这个类有一个无参构造函数，即使这个类已经定义了其它的构造函数
                                                                // 因为 Spring Data 需要通过反射来创建这个类的实例，以便进行数据持久化。
                                                                // 同时这个注解设定了：
                                                                //    - access = AccessLevel.PRIVATE，
                                                                //      即设定无参构造函数的访问权限是 private（私有），
                                                                //      这样就避免被反射之外的其它方式代码调用（例如使用Class.newInstance()或Constructor.newInstance()等方法）。
                                                                //    - force = true，
                                                                //      即设定基于无参构造函数创建的类实例属性的初始值为 0/null/false（根据相应属性的类型）。
                                                                // 为什么说是 *确保* 呢？因为：
                                                                //    - Java 会为每个类自动生成一个无参构造函数，但是只有当这个类没有定义其它构造函数时才会生成；
                                                                //    - 如果没有这个注解，那么当这个类已经定义了其它的构造函数时，Java 就不会为这个类自动生成无参构造函数了。
                                                                // 参考：书中 3.3.2 节 或 https://stackoverflow.com/questions/68314072/why-to-use-allargsconstructor-and-noargsconstructor-together-over-an-entity
                                                                // 注：虽然相关的参考或问答大都是针对 JPA 的，但不论是 Spring Data JDBC 还是 Spring Data JPA，其数据持久化过程都是需要通过反射来创建所需类实例的。
public class Ingredient /*implements Persistable<String>*/ {
                        // 注：使用 JPA 后，将持久化过程的实体状态检测（包括检测是否新建）可以交给 JPA 自动处理，
                        //     因此如果没有特别的需要，那么可以不用实现 Persistable 接口及其方法。
    
    @Id
    private String id;
    private String name;
    private Type type;

    public static enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }

    // 注：使用 JPA 后，将持久化过程的实体状态检测（包括检测是否新建）可以交给 JPA 自动处理，
    //     因此如果没有特别的需要，那么可以不用实现 Persistable 接口及其方法。
    /*@Override*/
    // @Transient  // 注：默认情况下这里无需使用 @Transient 注解，因为 Spring Data 默认的访问类型是 AccessType.FIELD，
                   //     也就是访问属性来持久化而不是访问 getter/setter，因此 new 不会被 Spring Data 访问并持久化到数据库
                   //     （如果要使用 getter/setter 的访问方式那么 Spring Data 的访问类型应设为 AccessType.PROPERTY）。
    /*public boolean isNew() {*/  // 注：isNew() 用于 Spring Data 在持久化之前调用它来判断对象是否是要新建的（is new）。
        /*return true;*/          //     它有几种可能的实现方式：
                              //
                              //     1. 一种实现方式是直接返回 true，这样 Spring Data 就会直接认为这个对象是新建的，
                              //        从而在持久化时直接执行 insert 操作，而不会执行 update 操作。
                              //        这种实现的一种可能应用场景是这个类对应的表只需要新增而不需要更新（例如演示用法的场景）。
                              // 
                              //     2. 另一种是实现方式是根据对象的 id 属性是否为 null 来判断这个对象是否是新建的。
                              //        例如：
                              //            @Override
                              //            public boolean isNew() {
                              //                return this.id == null;  // 如果 id 属性为 null，那么就认为这个对象是新建的
                              //            }
                              //        这样 Spring Data 就会根据 id 属性是否为 null 来判断这个对象是否是新建的，
                              //        从而在持久化时执行 insert 或 update 操作。
                              //        这种实现方式需要：
                              //            1) 应用层在调用 save(对象实例) 方法时，其中实例化对象时，如果是新建对象那么就不要设置 id 属性。
                              //            2) 搭配数据库相应表的主键预设的生成规则或 @GeneratedValue 注解来自动生成主键值。
                              //               注：
                              //                   1> 数据库表的主键预设生成规则是在创建表的时候指定的，例如：
                              //                       CREATE TABLE IF NOT EXISTS Cat (
                              //                           id            IDENTITY,  /* 指定 id 的生成规则为自增长 */
                              //                           ...
                              //                       )
                              //                   2> @GeneratedValue 注解的主键属性（例如 id）并不会在对象实例化时自动生成该主键属性的值，
                              //                   而是在持久化时，根据：
                              //                       - 指定的生成策略（例如 GenerationType.IDENTITY、GenerationType.AUTO 等）
                              //                       - 数据库的不同实现（例如 MySQL、PostgreSQL 等，注意有些数据库实现不支持某些主键生成策略）
                              //                   自动（例如自动构造相关数据库语句）生成主键字段的值。
                              //                   注：主键生成策略解释和用法可参考：
                              //                       - [GenerationType各类型解释](https://blog.51cto.com/u_15127550/4396255)
                              //                       - [GenerationType各类型用法](https://fanlychie.github.io/post/jpa-generatedvalue-annotation.html)
                              //        
                              //     3. 还有一种方式是根据对象是否保存到数据库或者从数据库中读取出来来判断这个对象是否是新建的。
                              //        这种方式需要搭配 Spring Data JPA 的 @PrePersist 和 @PostLoad 注解来实现。
                              //        例如：
                              //            [摘自官方示例](https://docs.spring.io/spring-data/jpa/docs/2.2.6.RELEASE/reference/html/#jpa.entity-persistence.saving-entites.strategies)
                              //            @MappedSuperclass  // 这个注解的作用是将这个类标记为超类，即这个类的属性会被子类继承。这是为了能实现一个抽象类封装一些通用逻辑（例如这里的 isNew 逻辑）以便继承来复用。
                              //            public abstract class AbstractEntity<ID> implements Persistable<ID> {
                              //
                              //                @Transient  // 这个注解的作用是将这个属性标记为非持久化属性，即这个属性不会被持久化到数据库。
                              //                private boolean isNew = true;   // 标记为私有属性，防止外部访问/修改。
                              //  
                              //                @Override
                              //                public boolean isNew() {
                              //                    return isNew; 
                              //                }
                              //  
                              //                @PrePersist  // 在应用层调用了依赖此实体类的 Repository 实现的 save() 方法后，保存到数据库之前，将 isNew 属性设为 false，因为如果需要保存到数据库那么就可以认为这个对象不是新建的。
                              //                @PostLoad    // 在从数据库中读取出来之后，将 isNew 属性设为 false，因为如果能从数据库中读取出来那么就可以这个对象不是新建的。
                              //                void markNotNew() {
                              //                    this.isNew = false;
                              //                }
                              //  
                              //                // More code…
                              //            }
    /*}*/

}
