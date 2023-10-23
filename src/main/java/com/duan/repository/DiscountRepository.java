package com.duan.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.duan.entity.DiscountEntity;

@Repository
public interface DiscountRepository extends JpaRepository<DiscountEntity, Integer>{
  boolean existsByDiscountCode(String discountCode); 
  Optional<DiscountEntity> findByDiscountCode(String discountCode);
}
