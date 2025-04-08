package com.example.shopping_mart.dto;

import com.example.shopping_mart.model.Category;
import com.example.shopping_mart.model.Image;
import com.example.shopping_mart.model.Product;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int inventory;
    private String brand;
    private Category category;
    private List<Image> images;

    public ProductDto(Long id,String name, String description, BigDecimal price, int inventory,
                      String brand, Category category, List<Image> images) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.inventory = inventory;
        this.brand = brand;
        this.category = category;
        this.images = images;
    }

    public ProductDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.inventory = product.getInventory();
        this.brand = product.getBrand();
        this.category = product.getCategory();
        this.images = product.getImages();
    }




}
