package com.duan.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.duan.entity.DetailOrderEntity;

public interface DetailOrderRepository extends JpaRepository<DetailOrderEntity, Integer>{
	List<DetailOrderEntity> findByOrderId(int orderId);
	Optional<DetailOrderEntity> findByOrderIdAndProductSizeId(int orderId, int productSizeId);
	Optional<DetailOrderEntity> findById(int detailOrderId);
	void deleteById(int detailOrderId);
}
