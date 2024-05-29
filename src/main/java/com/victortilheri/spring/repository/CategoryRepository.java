package com.victortilheri.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.victortilheri.spring.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{

}
