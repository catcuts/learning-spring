package com.example.demo.domain;

import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class Cat {

    private Long id;  // id 属性作为数据库表中的主键即 Cat 对象的唯一标识

    private Date createdAt = new Date();  // createdAt 属性用于存储 Cat 对象的创建时间
    
    @NotNull  // 校验以确保 name 属性的值不能为空
    @Size(min=5, message="Name must be at least 5 characters long")  // 校验以确保 name 属性的值至少包含 5 个字符
                                                                     // （否则会提示 message 属性中的消息）
    private String name;
    
    @NotNull  // 校验以确保 ingredients 属性的值不能为空
    @Size(min=1, message="You must choose at least 1 ingredient")  // 校验以确保 ingredients 属性的值（列表）至少包含 1 个元素
                                                                   // （否则会提示 message 属性中的消息）
    private List<Ingredient> ingredients;

}
