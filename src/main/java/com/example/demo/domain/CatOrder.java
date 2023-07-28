package com.example.demo.domain;

import java.util.List;
import java.util.ArrayList;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.CreditCardNumber;
import lombok.Data;

@Data
public class CatOrder {

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

    private List<Cat> cats = new ArrayList<>();

    public void addDesign(Cat cat) {
        this.cats.add(cat);
    }
    
}
