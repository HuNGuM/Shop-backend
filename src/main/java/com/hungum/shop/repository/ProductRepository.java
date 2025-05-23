package com.hungum.shop.repository;

import com.hungum.shop.model.Category;
import com.hungum.shop.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {
    Optional<Product> findByName(String productName);

    List<Product> findByCategory(Category category);

    Optional<Product> findBySku(String sku);
}
