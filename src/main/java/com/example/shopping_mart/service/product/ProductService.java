package com.example.shopping_mart.service.product;

import com.example.shopping_mart.exceptions.CategoryNotFoundException;
import com.example.shopping_mart.exceptions.ProductAlreadyExistsException;
import com.example.shopping_mart.exceptions.ProductNotFoundException;
import com.example.shopping_mart.model.Category;
import com.example.shopping_mart.model.Product;
import com.example.shopping_mart.repository.CategoryRepository;
import com.example.shopping_mart.repository.ProductRepository;
import com.example.shopping_mart.request.AddProductRequest;
import com.example.shopping_mart.request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Product addProduct(AddProductRequest request) {
        // Validate product name
        validateProductName(request.getName());

        // Retrieve or create the category
        Category category = getOrCreateCategory(request.getCategory().getName());

        // Create and save the product
        Product product = createProduct(request, category);

        // Set images to null
        product.setImages(null);
        return productRepository.save(product);
    }

    /**
     * Validates if a product with the same name already exists.
     * Throws an exception if found.
     */
    private void validateProductName(String name) {
        if (productRepository.existsByName(name)) {
            throw new ProductAlreadyExistsException(String.format("A product with name '" + name + "' already exists."));
        }
    }

    /**
     * Retrieves an existing category or creates a new one if not found.
     * Optimized to minimize database calls.
     */
    private Category getOrCreateCategory(String categoryName) {
        return categoryRepository.findByName(categoryName)
                .orElseGet(() -> saveNewCategory(categoryName));
    }

    /**
     * Saves a new category to the database.
     */
    private Category saveNewCategory(String categoryName) {
        Category newCategory = new Category(categoryName);
        return categoryRepository.save(newCategory);
    }

    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getDescription(),
                request.getInventory(),
                request.getPrice(),
                category
        );
    }

    @Override
    public Product getProductById(long id) {
        return productRepository.findById(id)
                .orElseThrow(()-> new ProductNotFoundException("product not Found!"));
    }

    @Override
    public void deleteProductById(long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " not found!"));
        productRepository.delete(product);
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepository::save)
                .orElseThrow(() -> new ProductNotFoundException("product not Found!"));
    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        Category category = getOrCreateCategory(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            throw new ProductNotFoundException("product not Found!");
        }
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        // Check if the category exists in the system
        boolean categoryExists = productRepository.existsByCategoryName(category); // Assume this method exists

        if (!categoryExists) {
            throw new CategoryNotFoundException("Category not found: " + category);
        }

        // If category exists, get the products by category
        List<Product> products = productRepository.findByCategoryName(category);

        if (products.isEmpty()) {
            throw new ProductNotFoundException("There are no products in that category!");
        }
        return products;
    }


    @Override
    public List<Product> getProductByBrand(String brand) {
        if (!productRepository.existsByBrand(brand)) {
            throw new ProductNotFoundException("brand  not found!");
        }

        List<Product> products = productRepository.findByBrand(brand);
        if (products.isEmpty()) {
            throw new ProductNotFoundException("There are no products in that brand!" + brand);
        }
        return products;
    }

    @Override
    public List<Product> getProductByCategoryAndBrand(String category, String brand) {
        if (!productRepository.existsByCategoryName(category)) {
            System.out.println("Category not found: " + category);
            throw new CategoryNotFoundException("Category not found: " + category);
        }else if (!productRepository.existsByBrand(brand)){
            throw new ProductNotFoundException("There is no products with this brand!" + brand);
        }

        List <Product> products = productRepository.findByCategoryNameAndBrand(category, brand);
        if (products.isEmpty()) {
            throw new ProductNotFoundException("There are no products with this brand and category!" + brand);
        }
        return products;
    }

    @Override
    public Optional<Product> getProductByName(String productName) {
        return Optional.ofNullable(productRepository.findByName(productName)
                .orElseThrow(() -> new ProductNotFoundException("product not Found!")));

    }

    @Override
    public List<Product> getProductByBrandAndNAme(String brand, String productName) {
        return productRepository.findByBrandAndName(brand, productName);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String productName) {
        try {
            Long count = productRepository.countByBrandAndName(brand, productName);
            // Optional: Throw an exception if count is null (depends on your logic)
            if (count == null) {
                throw new IllegalStateException("Unexpected null result from repository");
            }
            return count;
        } catch (Exception ex) {
            // Log the error (use a proper logger in production)
            System.err.println("Failed to count products by brand and name: " + ex.getMessage());
            // Wrap and rethrow as a custom or runtime exception
            throw new RuntimeException("Failed to retrieve product count due to an internal error", ex);
        }
    }
}
