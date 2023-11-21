package com.duan.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.duan.entity.ProductSizeEntity;

public interface ProductSizeRepository extends JpaRepository<ProductSizeEntity, Integer> {
	List<ProductSizeEntity> findByProductEntityProductId(int productId);

	@Query(
		value = 
		"SELECT COUNT(Product_Size_Id) " + 
		"FROM Product_Size INNER JOIN Product ON Product.Product_Id = Product_Size.Product_Id " +
		"WHERE Product_Size.Product_Id = ?2 AND Product_Size.Product_Size like ?1"
	, nativeQuery = true)
	Integer countByProductSizeAndProductId(String productSize, int productId);
}
