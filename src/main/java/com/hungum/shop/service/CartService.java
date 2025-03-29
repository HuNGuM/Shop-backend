package com.hungum.shop.service;

import com.hungum.shop.exceptions.ShopException;
import com.hungum.shop.model.Product;
import com.hungum.shop.model.ShoppingCart;
import com.hungum.shop.model.ShoppingCartItem;
import com.hungum.shop.repository.CartRepository;
import com.hungum.shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartService {

    private final ProductRepository productRepository;
    private final AuthService authService;
    private final CartRepository cartRepository;

    public ShoppingCart getCartForCurrentUser() {
        String username = authService.getCurrentUser()
                .orElseThrow(() -> new ShopException("User not authenticated"))
                .getUsername();

        return cartRepository.findByUsername(username) != null
                ? cartRepository.findByUsername(username)
                : new ShoppingCart(null, new HashSet<>(), BigDecimal.ZERO, 0, username);
    }

    public void addToCart(String sku) {
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new ShopException("Product with SKU " + sku + " not found"));

        ShoppingCart cart = getCartForCurrentUser();

        ShoppingCartItem item = new ShoppingCartItem();
        item.setName(product.getName());
        item.setPrice(product.getPrice());

        cart.getShoppingCartItems().add(item);
        cart.setNumberOfItems(cart.getShoppingCartItems().size());
        cart.setCartTotalPrice(cart.getShoppingCartItems().stream()
                .map(ShoppingCartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        cartRepository.save(cart);
    }

    public void removeFromCart(String productName) {
        ShoppingCart cart = getCartForCurrentUser();

        boolean removed = cart.getShoppingCartItems().removeIf(item -> item.getName().equals(productName));
        if (!removed) {
            throw new ShopException("Item not found in cart: " + productName);
        }

        cart.setNumberOfItems(cart.getShoppingCartItems().size());
        cart.setCartTotalPrice(cart.getShoppingCartItems().stream()
                .map(ShoppingCartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        cartRepository.save(cart);
    }

    public void clearCart() {
        ShoppingCart cart = getCartForCurrentUser();
        cart.getShoppingCartItems().clear();
        cart.setCartTotalPrice(BigDecimal.ZERO);
        cart.setNumberOfItems(0);
        cartRepository.save(cart);
    }

    public ShoppingCart getCart() {
        return getCartForCurrentUser();
    }
}
