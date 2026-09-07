package com.example.demotuan2_restfulapi.service;

import com.example.demotuan2_restfulapi.model.CartItem;
import com.example.demotuan2_restfulapi.model.Product;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class ShoppingCart implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final List<CartItem> items =
            new ArrayList<>();
    public void add(Product product) {
        for (CartItem item : items) {
            if (item.getProduct().getId()
                    == product.getId()) {
                item.increaseQuantity();
                return;
            }
        }
        items.add(new CartItem(product));
    }
    public List<CartItem> getItems() {
        return Collections.unmodifiableList(items);
    }
    public int getItemCount() {
        return items.stream().mapToInt(CartItem::getQuantity)
                .sum();
    }
    public BigDecimal getTotal() {
        return items.stream()
                .map(CartItem::getSubtotal)
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }
}
