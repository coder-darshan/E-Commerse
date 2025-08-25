package com.darshan.category_and_product.repository;

import com.darshan.category_and_product.entity.SubCategory;
import org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategory,Integer> {

    List<SubCategory> findByCategoryId(Long categoryId);
}
