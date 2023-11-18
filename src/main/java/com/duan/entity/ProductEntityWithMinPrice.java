package com.duan.entity;
import java.math.BigDecimal;
import java.util.Date;

public interface ProductEntityWithMinPrice{
  Integer getProductId();
  String getProductName();
  String getProductDescription();
  Boolean getProductIsPopular();
  Boolean getProductActive();
  Date getProductCreationDate();
  String getProductImageUrl();
  BigDecimal getProductMinPrice();
  CategoryEntity getCategoryEntity();
  interface CategoryEntity{
    Integer getCategoryId();
    String getCategoryName();
  }
}