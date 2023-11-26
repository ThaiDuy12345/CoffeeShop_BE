package com.duan.entity;

import jakarta.persistence.Embeddable;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackId {
  @Column(name = "Product_ID")
  private int productId;

  @Column(name = "Account_Phone")
  private String accountPhone;
}
