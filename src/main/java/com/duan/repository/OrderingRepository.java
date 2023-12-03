package com.duan.repository;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.duan.entity.OrderingEntity;
import com.duan.entity.OrderingEntityStatistic;

import jakarta.transaction.Transactional;

public interface OrderingRepository extends JpaRepository<OrderingEntity, Integer>{
  List<OrderingEntity> findAllByOrderingStatusAndAccountEntity_AccountPhone(int OrderingStatus, String accountPhone);
  List<OrderingEntity> findAllByAccountEntityAccountPhone(String accountPhone);
  List<OrderingEntity> findAllByUpdatedByAccountEntityAccountPhone(String accountPhone);
  Integer countByOrderingStatus(Integer orderingStatus);
  
  @Transactional
  @Query(value = 
    "SELECT " +
    "COALESCE(COUNT(*), 0) " + 
    "FROM Ordering o " + 
    "INNER JOIN Account a ON a.Account_Phone like o.Account_Phone " + 
    "INNER JOIN Detail_Order do ON do.Ordering_ID = o.Ordering_ID " +
    "INNER JOIN Product_Size ps ON ps.Product_Size_ID = do.Product_Size_ID " +
    "INNER JOIN Product p ON p.Product_ID = ps.Product_ID " +
    "WHERE p.Product_ID = ?1 AND a.Account_Phone like ?2 AND o.Ordering_Status = 4"
  , nativeQuery = true)
  Integer countOrderingQuantityFromAnAccountAndProduct(Integer productId, String accountPhone);

  @Query(value =
  "SELECT " +
  "  o.Ordering_Creation_Date as date, " +
  "  COALESCE(COUNT(*), 0) as orderingQuantity " +
  "FROM Ordering o " +
  "WHERE " +
  "  o.Ordering_Status = 4 AND " +
  "  o.Ordering_Creation_Date >= ?1 AND " +
  "  o.Ordering_Creation_Date <=  ?2 " +
  "GROUP BY o.Ordering_Creation_Date " +
  "ORDER BY o.Ordering_Creation_Date ASC"
  , nativeQuery = true)
  List<OrderingEntityStatistic> getOrderingStatisticsByDate(Date startDate, Date endDate);
}