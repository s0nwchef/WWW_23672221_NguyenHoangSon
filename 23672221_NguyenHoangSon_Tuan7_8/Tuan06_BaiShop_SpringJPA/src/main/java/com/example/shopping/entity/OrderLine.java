package com.example.shopping.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "orderlines")
public class OrderLine {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "order_id")
    private PurchaseOrder order;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "product_id")
    private Product product;
    private Integer amount;
    @Column(name = "purchasePrice")
    private BigDecimal purchasePrice;
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public PurchaseOrder getOrder() { return order; }
    public void setOrder(PurchaseOrder order) { this.order = order; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Integer getAmount() { return amount; }
    public void setAmount(Integer amount) { this.amount = amount; }
    public BigDecimal getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(BigDecimal purchasePrice) { this.purchasePrice = purchasePrice; }
    @Transient public BigDecimal getLineTotal() {
        return purchasePrice == null || amount == null ? BigDecimal.ZERO : purchasePrice.multiply(BigDecimal.valueOf(amount));
    }
}
