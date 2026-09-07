package com.example.demotuan2_restfulapi.model;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
public class CartItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Product product;
    private int quantity;
    public CartItem(Product product) {
        this.product = product;
        this.quantity = 1;
    }

    public void increaseQuantity() {
        quantity++;
    }
    public Product getProduct() {
        return product;
    }
    public int getQuantity() {
        return quantity;
    }
    public BigDecimal getSubtotal() {
        return
                product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
