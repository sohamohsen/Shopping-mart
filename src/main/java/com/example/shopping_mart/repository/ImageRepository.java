package com.example.shopping_mart.repository;

import com.example.shopping_mart.model.Image;
import com.example.shopping_mart.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends CrudRepository<Image, Long> {
    List <Image> findByProductId(Long productId);
}
