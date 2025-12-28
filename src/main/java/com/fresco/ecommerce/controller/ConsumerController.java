package com.fresco.ecommerce.controller;

import com.fresco.ecommerce.models.Cart;
import com.fresco.ecommerce.models.CartProduct;
import com.fresco.ecommerce.models.Product;
import com.fresco.ecommerce.models.UserInfo;
import com.fresco.ecommerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth/consumer")
public class ConsumerController {

    @Autowired
    ProductRepo productRepo;

    @Autowired
    CartRepo cartRepo;

    @Autowired
    CartProductRepo cpRepo;

    @Autowired
    UserInfoRepository userRepo;

    @GetMapping("/cart")
    public ResponseEntity<Object> getCart(Principal principal) {
        Optional<Cart> opt = cartRepo.findByUserUsername(principal.getName());
        return ResponseEntity.ok(opt.orElse(null));
    }

    @PostMapping("/cart")
    public ResponseEntity<Object> postCart(Principal principal, @RequestBody Product product) {
        // add product to cart for user
        UserInfo user = userRepo.findByUsername(principal.getName()).orElseThrow();
        Cart cart = cartRepo.findByUserUsername(principal.getName()).orElseThrow();
        Optional<CartProduct> existing = cpRepo.findByCartUserUserIdAndProductProductId(user.getUserId(), product.getProductId());
        if (existing.isPresent()) {
            return ResponseEntity.status(409).body("Product already in cart");
        }
        Product p = productRepo.findById(product.getProductId()).orElseThrow();
        CartProduct cp = new CartProduct(cart, p, 1);
        cpRepo.save(cp);
        cart.updateTotalAmount(p.getPrice());
        cartRepo.save(cart);
        return ResponseEntity.status(200).body(null);
    }

    @PutMapping("/cart")
    public ResponseEntity<Object> putCart(Principal principal,  @RequestBody CartProduct cp) {
        UserInfo user = userRepo.findByUsername(principal.getName()).orElseThrow();
        Cart cart = cartRepo.findByUserUsername(principal.getName()).orElseThrow();
        Product product = cp.getProduct();
        int productId = product.getProductId();
        Optional<CartProduct> existing = cpRepo.findByCartUserUserIdAndProductProductId(user.getUserId(), productId);
        if (existing.isPresent()) {
            CartProduct e = existing.get();
            if (cp.getQuantity() == 0) {
                // subtract amount
                cart.setTotalAmount(cart.getTotalAmount() - e.getProduct().getPrice() * e.getQuantity());
                cpRepo.deleteByCartUserUserIdAndProductProductId(user.getUserId(), productId);
            } else {
                // update quantity and total
                double old = e.getProduct().getPrice() * e.getQuantity();
                e.setQuantity(cp.getQuantity());
                cpRepo.save(e);
                double now = e.getProduct().getPrice() * e.getQuantity();
                cart.setTotalAmount(cart.getTotalAmount() - old + now);
            }
        } else {
            if (cp.getQuantity() > 0) {
                Product p = productRepo.findById(productId).orElseThrow();
                CartProduct newCp = new CartProduct(cart, p, cp.getQuantity());
                cpRepo.save(newCp);
                cart.setTotalAmount(cart.getTotalAmount() + p.getPrice() * cp.getQuantity());
            }
        }
        cartRepo.save(cart);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/cart")
    public ResponseEntity<Object> deleteCart(Principal principal,  @RequestBody Product product) {

        UserInfo user = userRepo.findByUsername(principal.getName()).orElseThrow();
        Optional<CartProduct> existing = cpRepo.findByCartUserUserIdAndProductProductId(user.getUserId(), product.getProductId());
        if (existing.isPresent()) {
            CartProduct e = existing.get();
            Cart cart = e.getCart();
            cart.setTotalAmount(cart.getTotalAmount() - e.getProduct().getPrice() * e.getQuantity());
            cpRepo.deleteByCartUserUserIdAndProductProductId(user.getUserId(), product.getProductId());
            cartRepo.save(cart);
        }
        return ResponseEntity.ok().build();
    }

}
