package com.darshan.category_and_product.repository;

import com.darshan.category_and_product.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
