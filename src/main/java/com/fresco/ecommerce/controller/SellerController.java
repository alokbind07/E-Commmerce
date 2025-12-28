package com.fresco.ecommerce.controller;

import com.fresco.ecommerce.models.Category;
import com.fresco.ecommerce.models.Product;
import com.fresco.ecommerce.models.UserInfo;
import com.fresco.ecommerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth/seller")
public class SellerController {

    @Autowired
    ProductRepo productRepo;

    @Autowired
    UserInfoRepository userRepo;

    @Autowired
    CategoryRepo categoryRepo;

    @PostMapping("/product")
    public ResponseEntity<Object> postProduct(Principal principal, @RequestBody Product product) {
        UserInfo seller = userRepo.findByUsername(principal.getName()).orElseThrow();
        // set seller object and category
        if (product.getCategory() != null) {
            Optional<Category> c = categoryRepo.findByCategoryName(product.getCategory().getCategoryName());
            c.ifPresent(product::setCategory);
        }
        product.setSeller(seller);
        Product saved = productRepo.save(product);
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/auth/seller/product/{id}")
                .buildAndExpand(saved.getProductId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/product")
    public ResponseEntity<Object> getAllProducts(Principal principal) {
        UserInfo seller = userRepo.findByUsername(principal.getName()).orElseThrow();
        List<Product> list = productRepo.findBySellerUserId(seller.getUserId());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<Object> getProduct(Principal principal,  @PathVariable Integer productId) {
        UserInfo seller = userRepo.findByUsername(principal.getName()).orElseThrow();
        Optional<Product> p = productRepo.findBySellerUserIdAndProductId(seller.getUserId(), productId);
        if (p.isPresent()) return ResponseEntity.ok(p.get());
        return ResponseEntity.status(404).build();
    }

    @PutMapping("/product")
    public ResponseEntity<Object> putProduct(Principal principal,  @RequestBody Product updatedProduct) {
        UserInfo seller = userRepo.findByUsername(principal.getName()).orElseThrow();
        if (updatedProduct.getProductId() == 0) return ResponseEntity.status(404).build();
        Optional<Product> p = productRepo.findBySellerUserIdAndProductId(seller.getUserId(), updatedProduct.getProductId());
        if (p.isEmpty()) return ResponseEntity.status(404).build();
        Product exist = p.get();
        exist.setProductName(updatedProduct.getProductName());
        exist.setPrice(updatedProduct.getPrice());
        if (updatedProduct.getCategory() != null) {
            Optional<Category> c = categoryRepo.findByCategoryName(updatedProduct.getCategory().getCategoryName());
            c.ifPresent(exist::setCategory);
        }
        productRepo.save(exist);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/product/{productId}")
    public ResponseEntity deleteProduct(Principal principal,  @PathVariable Integer productId) {
        UserInfo seller = userRepo.findByUsername(principal.getName()).orElseThrow();
        Optional<Product> p = productRepo.findBySellerUserIdAndProductId(seller.getUserId(), productId);
        if (p.isEmpty()) return ResponseEntity.status(404).build();
        productRepo.delete(p.get());
        return ResponseEntity.ok().build();
    }

}
