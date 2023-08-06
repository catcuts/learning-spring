package com.example.demo.domain;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.CreditCardNumber;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

// 如果使用 Spring Data JPA 那么需要用上面两个注解代替 Spring Data JDBC 的下面两个注解
// import org.springframework.data.relational.core.mapping.Table;
// import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import lombok.Data;

@Data
/*@Table*/
@Entity  // 使用 @Entity 代替 @Table 注解，以获得更多实体特性（包括实体映射、实体关系、实体生命周期）
public class CatOrder implements Serializable {
    // 注：实现 Serializable 接口的作用是使得 CatOrder 对象可以在网络上传输，例如在 HTTP 请求中传输。
    //     考虑到订单对象需要在客户端和服务端之间传输，因此需要实现 Serializable 接口。
    private static final long serialVersionUID = 1L;  // 指定序列化版本号，这个版本号会被用来验证序列化对象的版本是否与本地版本相同。
                                                      // 如果不指定，那么每次编译时都会生成一个新的版本号，这样就会导致反序列化失败。
                                                      // 详见：https://zhuanlan.zhihu.com/p/347246506

    @Id  // 这个注解的作用是声明 id 属性为数据库相应表（Cat_Order表）中的主键
    private Long id;  // id 属性作为数据库表中的主键即 CatOrder 对象的唯一标识

    private Date placedAt = new Date();  // placedAt 属性用于存储 CatOrder 对象的下单时间

    // 其它属性也会自动映射到数据库表中相应的字段，例如：name、placedAt、ingredients 等。
    // 也可以显式地使用 @Column 注解来指定属性与数据库表中字段的映射关系，例如：

    @Column("CUSTOMER_NAME")
    @NotBlank(message="Delivery name is required")
    private String deliveryName;

    @NotBlank(message="Street is required")
    private String deliveryStreet;

    @NotBlank(message="City is required")
    private String deliveryCity;

    @NotBlank(message="State is required")
    private String deliveryState;

    @NotBlank(message="Zip code is required")
    private String deliveryZip;

    @CreditCardNumber(message="Not a valid credit card number")  // 这个注解声明该属性的值必须是合法的信用卡号，
                                                                 // 它要能通过 Luhn 算法的检查。
                                                                 // 这能防止用户有意或无意地输入错误的数据，
                                                                 // 但并不能确保这个信用卡号真的分配给了某个账户，
                                                                 // 也不能保证这个账号能够用来进行支付。
    private String ccNumber;

    @Pattern(
        regexp="^(0[1-9]|1[0-2])([\\/])([1-9][0-9])$", 
        message="Must be formatted MM/YY"
    )  // 使用正则表达式来验证 ccExpiration 属性的值是否符合 MM/YY 格式
    private String ccExpiration;

    @Digits(
        integer=3, 
        fraction=0, 
        message="Invalid CVV"
    )  // 使用 @Digits 注解来验证 ccCVV 属性的值是否是一个三位数
    private String ccCVV;

    @OneToMany(cascade = CascadeType.ALL)  // @OneToMany 注解声明了这个实体类（CatOrder）与另一个实体类（Cat）建立的数据表上的一对多关系，
                                           // 也就是 CatOrder 表与 Cat 表之间通过 CatOrder 表的 id 字段建立的一对多关系，
                                           // 即一个 CatOrder 对象可以拥有多个 Cat 对象，它们被放入 cats 属性中。
                                           // 同时，这个注解还声明了级联操作，即当 CatOrder 对象（及其映射的表记录）被删除时，其所拥有的 Cat 对象（及其映射的表记录）也会被删除。
    private List<Cat> cats = new ArrayList<>();

    public void addDesign(Cat cat) {
        this.cats.add(cat);
    }
    
}
