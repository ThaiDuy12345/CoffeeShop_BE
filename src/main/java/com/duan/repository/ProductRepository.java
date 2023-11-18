package com.duan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.duan.entity.CategoryEntity;
import com.duan.entity.ProductEntity;
import com.duan.entity.ProductEntityWithMinPrice;

import jakarta.persistence.NamedNativeQueries;
import jakarta.persistence.NamedNativeQuery;


public interface ProductRepository extends JpaRepository<ProductEntity, Integer>{
	List<ProductEntity> findByCategoryEntity(CategoryEntity category);
	Boolean existsByProductId(Integer productId);

	@Query(value =
    "SELECT  " +
    "   p.productId as productId, " + 
    "   p.productName as productName, " + 
    "   p.productDescription as productDescription, " + 
    "   p.productIsPopular as productIsPopular, " +
    "   p.productActive as productActive, " + 
    "   p.productCreationDate as productCreationDate, " + 
    "   p.productImageUrl as productImageUrl, " + 
    "   p.categoryEntity as categoryEntity, " + 
    "   MIN(ps.productSizePrice) as productMinPrice " +
    "FROM ProductEntity p " +
    "INNER JOIN ProductSizeEntity ps ON ps.productEntity.productId = p.productId " +
    "GROUP BY p, p.categoryEntity"
	)
	List<ProductEntityWithMinPrice> findAllProductWithMinPrice();
}
