package com.example.shopping_mart.service.product;

import com.example.shopping_mart.model.Product;
import com.example.shopping_mart.request.AddProductRequest;
import com.example.shopping_mart.request.ProductUpdateRequest;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    Product addProduct(AddProductRequest product);
    Product getProductById(long id);
    void deleteProductById(long id);
    Product updateProduct(ProductUpdateRequest request, long productId);

    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductByBrand(String brand);
    List<Product> getProductByCategoryAndBrand(String category, String brand);
    Optional<Product> getProductByName(String productName);
    List<Product> getProductByBrandAndNAme(String brand, String productName);
    Long countProductsByBrandAndName(String brand, String productName);


}
