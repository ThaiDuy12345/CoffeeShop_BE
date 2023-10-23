package com.duan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.duan.entity.CategoryEntity;
import com.duan.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer>{
	List<ProductEntity> findByCategory(CategoryEntity category);
}
