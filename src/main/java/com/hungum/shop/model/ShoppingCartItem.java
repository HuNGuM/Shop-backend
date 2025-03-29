package com.hungum.shop.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShoppingCartItem {
    private String name;
    private BigDecimal price;
}
