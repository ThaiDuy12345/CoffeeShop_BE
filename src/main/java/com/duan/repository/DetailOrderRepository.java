package com.duan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.duan.entity.DetailOrderEntity;
import com.duan.entity.DetailOrderId;

public interface DetailOrderRepository extends JpaRepository<DetailOrderEntity, DetailOrderId>{
	List<DetailOrderEntity> findAllByDetailOrderIdOrderingId(int orderingId);
}
