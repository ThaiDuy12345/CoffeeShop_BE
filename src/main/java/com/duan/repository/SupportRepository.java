package com.duan.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.duan.entity.SupportEntity;
import com.duan.entity.SupportEntityStatistic;

public interface SupportRepository extends JpaRepository<SupportEntity, Integer>{
  Integer countBySupportStatus(Boolean supportStatus);

  @Query(value = 
  "SELECT " + 
  "  s.Support_Creation_Date as date, " + 
  "  COUNT(*) as supportQuantity " + 
  "FROM Support s " + 
  "WHERE " + 
  "  s.Support_Creation_date >= ?1 AND " + 
  "  s.Support_Creation_date <= ?2 " + 
  "GROUP BY s.Support_Creation_Date " +
  "ORDER BY s.Support_Creation_Date ASC"
  , nativeQuery = true)
  List<SupportEntityStatistic> getSupportStatisticsByDate(Date startDate, Date endDate);
}
