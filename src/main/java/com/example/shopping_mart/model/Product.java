package com.example.shopping_mart.model;

import com.example.shopping_mart.dto.ProductDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int inventory;
    private String brand;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonIgnore
    private List<Image> images;

    public Product(String name, String brand, String description, int inventory, BigDecimal price, Category category) {
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.inventory = inventory;
        this.price = price;
        this.category = category;
    }

    public Product(ProductDto productDto){
        this.name = productDto.getName();
        this.brand = productDto.getBrand();
        this.description = productDto.getDescription();
        this.inventory = productDto.getInventory();
        this.price = productDto.getPrice();
        this.category = productDto.getCategory();
        this.images = productDto.getImages();
    }
}
