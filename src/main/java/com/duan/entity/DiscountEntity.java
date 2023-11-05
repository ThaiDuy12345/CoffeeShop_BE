package com.duan.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Discount")
@Data
public class DiscountEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Discount_ID")
    private int discountId;

    @Column(name = "Discount_Code", nullable = false, length = 255)
    private String discountCode;

    @Column(name = "Discount_Creation_Date", nullable = false, columnDefinition = "datetime2 default(GETDATE())")
    private Date discountCreationDate;

    @Column(name = "Discount_Expired_Date", nullable = false, columnDefinition = "datetime2 default(GETDATE())")
    private Date discountExpiredDate;

    @Column(name = "Discount_Minimum_Order_Price", nullable = false, columnDefinition = "decimal(18, 2) default(0) check(Discount_Minimum_Order_Price >= 0)")
    private BigDecimal discountMinimumOrderPrice;

    @Column(name = "Discount_Amount", nullable = false, columnDefinition = "decimal(18, 2) default(1000) check(Discount_Amount > 0)")
    private BigDecimal discountAmount;
		
		@JsonIgnore
		@OneToMany(mappedBy = "discountEntity")
    private List<OrderingEntity> orderingEntities;
}
