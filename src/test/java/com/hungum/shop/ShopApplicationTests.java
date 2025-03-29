package com.hungum.shop;

import com.hungum.shop.model.Category;
import com.hungum.shop.model.Product;
import com.hungum.shop.repository.CategoryRepository;
import com.hungum.shop.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ShopApplicationTests {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void category() {


        if (categoryRepository.findByName("Tablets").isEmpty()) {
            Category tablets = new Category();
            tablets.setName("Tablets");
            categoryRepository.save(tablets);
        }

        if (categoryRepository.findByName("Laptops").isEmpty()) {
            Category laptops = new Category();
            laptops.setName("Laptops");
            categoryRepository.save(laptops);
        }

        if (categoryRepository.findByName("Gaming").isEmpty()) {
            Category gaming = new Category();
            gaming.setName("Gaming");
            categoryRepository.save(gaming);
        }

        if (categoryRepository.findByName("Cameras").isEmpty()) {
            Category cameras = new Category();
            cameras.setName("Cameras");
            categoryRepository.save(cameras);
        }

        if (categoryRepository.findByName("Smart Watches").isEmpty()) {
            Category smartWatches = new Category();
            smartWatches.setName("Smart Watches");
            categoryRepository.save(smartWatches);
        }

        if (categoryRepository.findByName("Headphones & Headsets").isEmpty()) {
            Category headphones = new Category();
            headphones.setName("Headphones & Headsets");
            categoryRepository.save(headphones);
        }



        Category tabletsCategory = categoryRepository.findByName("Tablets").orElseThrow(() -> new IllegalArgumentException("Invalid Category"));
        List<Product> tabletProducts = productRepository.findByCategory(tabletsCategory);
        tabletsCategory.setPossibleFacets(Arrays.asList("Brand", "4G", "Battery Type", "Memory(RAM)", "Operating System Type", "SIM Type", "Primary Camera"));
        tabletProducts.forEach(product -> product.setCategory(tabletsCategory));
        categoryRepository.save(tabletsCategory);
        productRepository.saveAll(tabletProducts);

        Category laptopsCategory = categoryRepository.findByName("Laptops").orElseThrow(() -> new IllegalArgumentException("Invalid Category"));
        List<Product> laptopProducts = productRepository.findByCategory(laptopsCategory);
        laptopsCategory.setPossibleFacets(Arrays.asList("Brand", "Battery Type", "Display Type", "Graphics Card - Brand", "Graphics Card - Sub-Brand",
                "Hard Drive", "HDMI", "Memory (RAM)", "Hard Drive Type", "Operating System Type", "Processor Brand", "Core Type", "Touch Screen"));
        laptopProducts.forEach(product -> product.setCategory(laptopsCategory));
        categoryRepository.save(laptopsCategory);
        productRepository.saveAll(laptopProducts);

        Category gamingCategory = categoryRepository.findByName("Gaming").orElseThrow(() -> new IllegalArgumentException("Invalid Category"));
        List<Product> gamingProducts = productRepository.findByCategory(gamingCategory);
        gamingCategory.setPossibleFacets(Arrays.asList("Brand", "Console Type", "Display Type", "Internal Storage"));
        gamingProducts.forEach(product -> product.setCategory(gamingCategory));
        categoryRepository.save(gamingCategory);
        productRepository.saveAll(gamingProducts);

        Category camerasCategory = categoryRepository.findByName("Cameras").orElseThrow(() -> new IllegalArgumentException("Invalid Category"));
        List<Product> camerasProducts = productRepository.findByCategory(camerasCategory);
        camerasCategory.setPossibleFacets(Arrays.asList("Brand", "Battery Type", "Model", "Wi-Fi", "USB", "Focal Length", "HDMI", "Sensor Type", "Display Type"));
        camerasProducts.forEach(product -> product.setCategory(camerasCategory));
        categoryRepository.save(camerasCategory);
        productRepository.saveAll(camerasProducts);

        Category smartWatchesCategory = categoryRepository.findByName("Smart Watches").orElseThrow(() -> new IllegalArgumentException("Invalid Category"));
        List<Product> smartWatchProducts = productRepository.findByCategory(smartWatchesCategory);
        smartWatchesCategory.setPossibleFacets(Arrays.asList("Brand", "Battery Type", "Display", "Display Type", "Gender", "Watch Shape"));
        smartWatchProducts.forEach(product -> product.setCategory(smartWatchesCategory));
        categoryRepository.save(smartWatchesCategory);
        productRepository.saveAll(smartWatchProducts);

        Category headphonesCategory = categoryRepository.findByName("Headphones & Headsets").orElseThrow(() -> new IllegalArgumentException("Invalid Category"));
        List<Product> headphonesAndHeadsets = productRepository.findByCategory(headphonesCategory);
        headphonesCategory.setPossibleFacets(Arrays.asList("Brand", "Wireless", "Bluetooth", "USB", "Noise Cancellation", "Microphone Type"));
        headphonesAndHeadsets.forEach(product -> product.setCategory(headphonesCategory));
        categoryRepository.save(headphonesCategory);
        productRepository.saveAll(headphonesAndHeadsets);
    }

    @Test
    public void updateSku() {
        List<Product> products = productRepository.findAll();
        products.forEach(product -> {
            String sku = product.getSku();
            String newSku = sku.replace(" ", "-").replace("/", "-");
            product.setSku(newSku);
        });
        productRepository.saveAll(products);
    }
}
