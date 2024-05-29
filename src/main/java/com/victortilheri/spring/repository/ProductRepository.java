package com.victortilheri.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.victortilheri.spring.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

}
