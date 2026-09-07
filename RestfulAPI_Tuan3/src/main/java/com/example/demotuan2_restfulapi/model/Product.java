package com.example.demotuan2_restfulapi.model;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
public class Product implements Serializable {
    @Serial
    private static final long erialVersionUID = 1L;
    private final long id;
    private final String name;
    private final BigDecimal price;
    public Product(long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
    public long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public BigDecimal getPrice() {
        return price;
    }
}
