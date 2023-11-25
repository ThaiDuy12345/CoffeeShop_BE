package com.duan.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.duan.entity.OrderingEntity;

import jakarta.transaction.Transactional;

public interface OrderingRepository extends JpaRepository<OrderingEntity, Integer>{
  List<OrderingEntity> findAllByOrderingStatusAndAccountEntity_AccountPhone(int OrderingStatus, String accountPhone);
  List<OrderingEntity> findAllByAccountEntityAccountPhone(String accountPhone);
  List<OrderingEntity> findAllByUpdatedByAccountEntityAccountPhone(String accountPhone);
  
  @Transactional
  @Query(value = 
    "SELECT " +
    "COALESCE(COUNT(*), 0) " + 
    "FROM Ordering o " + 
    "INNER JOIN Account a ON a.Account_Phone like o.Account_Phone " + 
    "INNER JOIN Detail_Order do ON do.Ordering_ID = o.Ordering_ID " +
    "INNER JOIN Product_Size ps ON ps.Product_Size_ID = do.Product_Size_ID " +
    "INNER JOIN Product p ON p.Product_ID = ps.Product_ID " +
    "WHERE p.Product_ID = ?1 AND a.Account_Phone like ?2"
  , nativeQuery = true)
  Integer countOrderingQuantityFromAnAccountAndProduct(Integer productId, String accountPhone);
}