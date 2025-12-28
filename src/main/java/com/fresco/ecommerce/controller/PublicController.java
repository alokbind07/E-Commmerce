package com.fresco.ecommerce.controller;

import com.fresco.ecommerce.models.Product;
import com.fresco.ecommerce.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    @Autowired
    ProductRepo productRepo;

    @GetMapping("/product/search")
    public List<Product> getProducts(@RequestParam String keyword) {
        // Defensive: if keyword null/empty, let Spring handle missing param (test expects 400 when missing)
        return productRepo.findByProductNameContainingIgnoreCaseOrCategoryCategoryNameContainingIgnoreCase(keyword, keyword);
    }
}
