package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.demo.domain.Cat;

public interface CatRepository extends PagingAndSortingRepository<Cat, Long> {

    // 注：需使用 PagingAndSortingRepository 以便支持分页的 findAll，
    //     它 继承了 CrudRepository 并覆写了 findAll 方法以便支持分页。
    //     如果使用 CrudRepository 那么 findAll(pageReqeust) 时会报错。
    
}
