package com.duan.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

		

	public DiscountEntity(int discountId, String discountCode, Date discountCreationDate, Date discountExpiredDate,
			BigDecimal discountMinimumOrderPrice, BigDecimal discountAmount) {
		this.discountId = discountId;
		this.discountCode = discountCode;
		this.discountCreationDate = discountCreationDate;
		this.discountExpiredDate = discountExpiredDate;
		this.discountMinimumOrderPrice = discountMinimumOrderPrice;
		this.discountAmount = discountAmount;
	}

	public int getDiscountId() {
		return discountId;
	}

	public void setDiscountId(int discountId) {
		this.discountId = discountId;
	}

	public String getDiscountCode() {
		return discountCode;
	}

	public void setDiscountCode(String discountCode) {
		this.discountCode = discountCode;
	}

	public Date getDiscountCreationDate() {
		return discountCreationDate;
	}

	public void setDiscountCreationDate(Date discountCreationDate) {
		this.discountCreationDate = discountCreationDate;
	}

	public Date getDiscountExpiredDate() {
		return discountExpiredDate;
	}

	public void setDiscountExpiredDate(Date discountExpiredDate) {
		this.discountExpiredDate = discountExpiredDate;
	}

	public BigDecimal getDiscountMinimumOrderPrice() {
		return discountMinimumOrderPrice;
	}

	public void setDiscountMinimumOrderPrice(BigDecimal discountMinimumOrderPrice) {
		this.discountMinimumOrderPrice = discountMinimumOrderPrice;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
}
