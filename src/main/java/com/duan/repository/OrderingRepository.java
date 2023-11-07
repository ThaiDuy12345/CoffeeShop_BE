package com.duan.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.duan.entity.OrderingEntity;

public interface OrderingRepository extends JpaRepository<OrderingEntity, Integer>{
  List<OrderingEntity> findAllByOrderingStatusAndAccountEntity_AccountPhone(int OrderingStatus, String accountPhone);
}
