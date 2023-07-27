package com.example.demo.domain;

import java.util.List;
import java.util.ArrayList;
import lombok.Data;

@Data
public class CatOrder {

    private String deliveryName;
    private String deliveryStreet;
    private String deliveryCity;
    private String deliveryState;
    private String deliveryZip;
    private String ccNumber;
    private String ccExpiration;
    private String ccCVV;

    private List<Cat> cats = new ArrayList<>();

    public void addDesign(Cat cat) {
        this.cats.add(cat);
    }
    
}
