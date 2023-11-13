package com.duan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.duan.entity.DetailOrderEntity;
import com.duan.entity.DetailOrderId;

import jakarta.transaction.Transactional;

public interface DetailOrderRepository extends JpaRepository<DetailOrderEntity, DetailOrderId>{
	List<DetailOrderEntity> findAllByDetailOrderIdOrderingId(int orderingId);
	
	@Modifying(clearAutomatically	= true, flushAutomatically = true)
	@Transactional
	@Query(value = 
		"INSERT INTO [Detail_Order] (Detail_Order_Product_Quantity, Ordering_ID, Product_Size_ID) " + 
		"VALUES (?1, ?2, ?3)"
	, nativeQuery = true)
	void insertNewDetailOrder(int detailOrderProductQuantity, int orderingId, int productSizeId);
	
	@Modifying(clearAutomatically	= true, flushAutomatically = true)
	@Transactional
	@Query(value = 
		"UPDATE [Detail_Order] " + 
		"SET Detail_Order_Product_Quantity = ?1 " + 
		"WHERE Ordering_ID = ?2 AND Product_Size_ID = ?3"
	, nativeQuery = true)
	void updateDetailOrder(int detailOrderProductQuantity, int orderingId, int productSizeId);

	@Query(value = 
		"SELECT SUM(do.Detail_Order_Product_Quantity) from Detail_Order do " + 
		"WHERE do.Ordering_ID = ?1"
	, nativeQuery = true)
	Integer getSumProductInTheOrdering(int orderingId);
}
