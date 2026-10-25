package com.example.shopping.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "text")
    private String text;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "product_id")
    private Product product;
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
}
