package com.duan.repository;


import org.springframework.data.jpa.repository.JpaRepository;


import com.duan.entity.OrderingEntity;

public interface OrderingRepository extends JpaRepository<OrderingEntity, Integer>{
}
