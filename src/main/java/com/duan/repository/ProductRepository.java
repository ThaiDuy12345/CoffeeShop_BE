package com.duan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.duan.entity.CategoryEntity;
import com.duan.entity.ProductEntity;
import com.duan.entity.ProductEntityWithMinPrice;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer>{
	List<ProductEntity> findByCategoryEntity(CategoryEntity category);
	Boolean existsByProductId(Integer productId);
    Integer countByProductActive(Boolean productActive);
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


    @Query(value = 
    "SELECT " +
    "   COALESCE(SUM(do.Detail_Order_Product_Quantity), 0) " +
    "FROM Detail_Order do " +
    "INNER JOIN Ordering o ON o.Ordering_ID = do.Ordering_ID " +
    "INNER JOIN Product_Size ps ON do.Product_Size_ID = ps.Product_Size_ID " +
    "INNER JOIN Product p ON p.Product_ID = ps.Product_ID " +
    "WHERE p.Product_ID  = ?1 AND o.Ordering_Status IN (1, 2, 3, 4)" 
    , nativeQuery = true)
    Integer getSoldQuantityByProductId(Integer productId);
}
