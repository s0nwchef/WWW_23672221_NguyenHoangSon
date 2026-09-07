package com.example.demotuan2_restfulapi.session;

import com.example.demotuan2_restfulapi.service.ShoppingCart;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.SessionScoped;
import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;
@SessionScoped
public class UserSession implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String instanceId =
            UUID.randomUUID().toString();
    private final ShoppingCart shoppingCart = new ShoppingCart();
    private String username;
    @PostConstruct
    public void created() {
        System.out.println(
                "CREA TED UserSession: " + instanceId
        );
    }
    @PreDestroy
    public void destroyed() {
        System.out.println(
                "DESTROYED UserSession: " + instanceId
        );
    }
    public void login(String username) {
        this.username = username;
    }
    public boolean isLoggedIn() {
        return username != null;
    }
    public String getUsername() {
        return username;
    }
    public String getInstanceId() {
        return instanceId;
    }
    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }
}
