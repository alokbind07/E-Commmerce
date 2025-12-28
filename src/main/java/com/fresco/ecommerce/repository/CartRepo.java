package com.fresco.ecommerce.repository;

import com.fresco.ecommerce.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CartRepo extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByUserUsername(String username);
}
