package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.domain.CatOrder;

public interface CatOrderRepository extends CrudRepository<CatOrder, Long> {

    /* 
        因为所继承的 CrudRepository 接口已定义常见的 findAll、findById、save 等操作，
        所以这里不需要再定义这些方法了。
        并且也不需要实现这些方法，因为 Spring Data 会在运行时自动实现这些方法。
    */

    /*
    CatOrder save(CatOrder order);
    */

}
