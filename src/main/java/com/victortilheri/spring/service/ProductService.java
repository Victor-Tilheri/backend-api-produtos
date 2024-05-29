package com.victortilheri.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

import com.victortilheri.spring.dto.ProductDTO;
import com.victortilheri.spring.model.Category;
import com.victortilheri.spring.model.Product;

import com.victortilheri.spring.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    public List<Product> getAllProducts(String orderBy, String orderDirection) {
        Sort.Direction direction = Sort.Direction.ASC;
        if (orderDirection.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }
        
        return productRepository.findAll(Sort.by(direction, orderBy));
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow();
    }

    public Product createProduct(ProductDTO productDTO) {
        Category category;

        if (productDTO.getCategoryId() != null) {
            category = categoryService.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
        } else if (productDTO.getCategoryName() != null && !productDTO.getCategoryName().isEmpty()) {
            category = new Category();
            category.setName(productDTO.getCategoryName());
            category = categoryService.save(category);
        } else {
            throw new RuntimeException("Category information is missing");
        }

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setCategory(category);

        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(updatedProduct.getName());
                    existingProduct.setPrice(updatedProduct.getPrice());
                    existingProduct.setUpdatedAt(Instant.now());
                    return productRepository.save(existingProduct);
                })
                .orElseThrow(() -> new NoSuchElementException("Product not found with id: " + id));
    }

    public boolean deleteProduct(Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    productRepository.deleteById(id);
                    return true;
                })
                .orElseThrow(() -> new NoSuchElementException("Product not found with id: " + id));
    }
}
