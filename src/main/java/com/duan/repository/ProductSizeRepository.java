package com.duan.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.duan.entity.ProductSizeEntity;

public interface ProductSizeRepository extends JpaRepository<ProductSizeEntity, Integer> {
	List<ProductSizeEntity> findByProduct_ProductId(int productId);
}
