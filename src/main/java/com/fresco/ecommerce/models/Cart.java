package com.fresco.ecommerce.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Cart {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int cartId;
    private double totalAmount;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    @JsonIgnore
    private UserInfo user;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "cart")
    private List<CartProduct> cartProducts;

    public Cart() { super(); }
    public Cart(Double totalAmount, UserInfo user) { this.totalAmount = totalAmount; this.user = user; }
    public Integer getCartId() { return cartId; }
    public void setCartId(Integer cartId) { this.cartId = cartId; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public UserInfo getUser() { return user; }
    public void setUser(UserInfo user) { this.user = user; }
    public List<CartProduct> getCartProducts() { return cartProducts; }
    public void setCartProducts(List<CartProduct> cartProducts) { this.cartProducts = cartProducts; }
    public void updateTotalAmount(Double price) { this.totalAmount += price; }
}
