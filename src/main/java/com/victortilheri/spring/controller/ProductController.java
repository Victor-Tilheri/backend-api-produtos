package com.victortilheri.spring.controller;

import org.springframework.web.bind.annotation.RestController;

import com.victortilheri.spring.dto.ProductDTO;
import com.victortilheri.spring.model.Category;
import com.victortilheri.spring.model.Product;
import com.victortilheri.spring.service.CategoryService;
import com.victortilheri.spring.service.ProductService;

import jakarta.validation.Valid;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@Validated
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @GetMapping
public ResponseEntity<List<ProductDTO>> getAllProducts(
        @RequestParam(required = false, defaultValue = "id") String orderBy,
        @RequestParam(required = false, defaultValue = "asc") String orderDirection
) {
    List<Product> products = productService.getAllProducts(orderBy, orderDirection);
    List<ProductDTO> productDTOs = new ArrayList<>();

    for (Product product : products) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setPrice(product.getPrice());
        productDTO.setCategoryId(product.getCategory().getId());
        productDTO.setCategoryName(product.getCategory().getName()); // Obtendo o nome da categoria

        productDTOs.add(productDTO);
    }

    return ResponseEntity.ok(productDTOs);
}
    
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductDTO productDTO) {
        if (productDTO.getCategoryId() == null) {
            return ResponseEntity.badRequest().body("categoryId is required");
        }

        // Encontrar a categoria pelo ID
        Category category = categoryService.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setCategory(category);

        Product savedProduct = productService.save(product);
        return ResponseEntity.ok(savedProduct);
    }

    
    
    @PutMapping("{id}")
    public ResponseEntity<?> updateProduct(@Valid @PathVariable Long id, @RequestBody ProductDTO productDTO) {
        if (productDTO.getCategoryId() == null) {
            return ResponseEntity.badRequest().body("categoryId is required");
        }

        // Encontrar a categoria pelo ID
        Category category = categoryService.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = productService.findById(id);
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setCategory(category);
        product.setUpdatedAt(Instant.now());

        Product updatedProduct = productService.save(product);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        boolean deleted = productService.deleteProduct(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
