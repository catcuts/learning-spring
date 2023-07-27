package com.example.demo.domain;

import java.util.List;
import lombok.Data;

@Data
public class Cat {
    
    private String name;
    
    private List<Ingredient> ingredients;

}
