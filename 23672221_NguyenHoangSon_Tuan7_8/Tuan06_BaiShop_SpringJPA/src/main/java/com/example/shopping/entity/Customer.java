package com.example.shopping.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
public class Customer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Column(name = "customerSince")
    private LocalDate customerSince;
    @OneToMany(mappedBy = "customer")
    @OrderBy("orderDate desc")
    private List<PurchaseOrder> orders = new ArrayList<>();
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDate getCustomerSince() { return customerSince; }
    public void setCustomerSince(LocalDate customerSince) { this.customerSince = customerSince; }
    public List<PurchaseOrder> getOrders() { return orders; }
    public void setOrders(List<PurchaseOrder> orders) { this.orders = orders; }
}
