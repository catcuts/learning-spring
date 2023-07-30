package com.example.demo.repository;

import java.util.Optional;

import com.example.demo.domain.CatOrder;

public interface CatOrderRepository {

    CatOrder save(CatOrder order);

}
