package com.duan.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.duan.entity.FavoriteProductEntity;
import com.duan.entity.FavoriteProductId;

public interface FavoriteProductRepository extends JpaRepository<FavoriteProductEntity, FavoriteProductId>{
  List<FavoriteProductEntity> findAllByFavoriteProductIdAccountPhone(String accountPhone);  
  Boolean existsByFavoriteProductIdAccountPhoneAndFavoriteProductIdProductId(String accountPhone, Integer ProductId);
}
