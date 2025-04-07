package com.example.shopping_mart.controller;

import com.example.shopping_mart.exceptions.ProductAlreadyExistsException;
import com.example.shopping_mart.exceptions.ProductNotFoundException;
import com.example.shopping_mart.exceptions.ResourceNotFoundException;
import com.example.shopping_mart.model.Product;
import com.example.shopping_mart.request.AddProductRequest;
import com.example.shopping_mart.request.ProductUpdateRequest;
import com.example.shopping_mart.response.ApiResponse;
import com.example.shopping_mart.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.http.HttpStatus.*;


import java.util.List;
import java.util.Optional;

import static org.hibernate.grammars.hql.HqlParser.CONFLICT;
import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> geAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            return ResponseEntity.ok(new ApiResponse("Get All Products", products));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error", INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ApiResponse> geProductById(@PathVariable int id) {
        try {
            Product product = productService.getProductById(id);
            return ResponseEntity.ok(new ApiResponse("Get Product by ID", product));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse> geProductByName(@PathVariable String name) {
        try {
            Optional<Product> product = productService.getProductByName(name);
            return ResponseEntity.ok(new ApiResponse("Get Product by ID", product));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/addproduct")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product) {
        try {
            Product newProduct = productService.addProduct(product);
            // Return 201 Created status since a new product was created
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Product added successfully.", newProduct));
        } catch (ProductAlreadyExistsException e) {
            // Return 409 Conflict if the product already exists
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            // Return 500 Internal Server Error for any unexpected issues
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("An error occurred: " + e.getMessage(), null));
        }
    }

        @PutMapping("/updateproduct/{id}")
        public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateRequest request, @PathVariable long id) {
        try {
            Product product = productService.updateProduct(request, id);
            return ResponseEntity.ok(new ApiResponse("Product updated", product));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/deleteproduct/{id}")
     public ResponseEntity<ApiResponse> deleteProduct(@PathVariable long id){
        try {
            productService.deleteProductById(id);
            return ResponseEntity.ok(new ApiResponse("Product Deleted!", id));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }

    }
}