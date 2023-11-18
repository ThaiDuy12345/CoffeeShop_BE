package com.duan.entity;

import jakarta.persistence.Embeddable;

import jakarta.persistence.Column;
import lombok.Data;

@Embeddable
@Data
public class FeedbackId {
  @Column(name = "Product_ID")
  private int productId;

  @Column(name = "Account_Phone")
  private String accountPhone;
}
