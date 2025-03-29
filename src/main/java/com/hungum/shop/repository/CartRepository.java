package com.hungum.shop.repository;

import com.hungum.shop.model.ShoppingCart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends CrudRepository<ShoppingCart, String> {
    ShoppingCart findByUsername(String username);
}

