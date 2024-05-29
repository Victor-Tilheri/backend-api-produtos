package com.victortilheri.spring;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.victortilheri.spring.model.Category;
import com.victortilheri.spring.model.Product;
import com.victortilheri.spring.repository.CategoryRepository;
import com.victortilheri.spring.repository.ProductRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository; 

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        Category cat1 = categoryRepository.save(new Category("Acessórios"));
        Category cat2 = categoryRepository.save(new Category("Notebooks"));
        Category cat3 = categoryRepository.save(new Category("Smartphones"));
        Category cat4 = categoryRepository.save(new Category("Tablets"));

        productRepository.save(new Product("Cabo Carregador", new BigDecimal("74.9"), cat1));
        productRepository.save(new Product("MacBook 12 Pro", new BigDecimal("9500.0"), cat2));
        productRepository.save(new Product("iPhone 15 Pro Max", new BigDecimal("6985.0"), cat3));
        productRepository.save(new Product("iPad 12", new BigDecimal("3250.0"), cat4));
    }
}
