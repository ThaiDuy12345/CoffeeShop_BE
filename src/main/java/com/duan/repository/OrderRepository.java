package com.duan.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.duan.entity.OrderingEntity;

public interface OrderRepository extends JpaRepository<OrderingEntity, Integer>{
    Optional<OrderingEntity> findByOrderID(int orderId);
}
