package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.domain.CatOrder;

public interface CatOrderRepository extends CrudRepository<CatOrder, Long> {

    /* 
        因为所继承的 CrudRepository 接口已定义常见的 findAll、findById、save 等操作，
        所以这里不需要再定义这些方法了。
        并且也不需要实现这些方法，因为 Spring Data 会在运行时自动实现这些方法。
        当然，Spring Data 自动实现这些方法的前提是相关的领域类要遵循一定的规范，例如：
            - 相关的领域类要实现 Serializable 接口；
            - 相关的领域类中要用 @Id 注解标注主键属性；
            - ...其它按需可选项（例如 @Table）
     */

    /*
    CatOrder save(CatOrder order);
    */

}
