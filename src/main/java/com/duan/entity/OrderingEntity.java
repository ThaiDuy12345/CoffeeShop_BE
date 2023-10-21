package com.duan.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.core.annotation.Order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Ordering")
@Data
public class OrderingEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Ordering_ID")
    private int orderingID;

    @Column(name = "Ordering_Status", nullable = false, columnDefinition = "int default 0 check(Ordering_Status in (0, 1, 2, 3, 4, -1))")
    private int orderingStatus;

    @Column(name = "Ordering_Creation_Date", nullable = false, columnDefinition = "datetime2 default(GETDATE())")
    private LocalDateTime orderingCreationDate;

    @Column(name = "Ordering_Shipping_Fee", nullable = false, columnDefinition = "decimal(18, 2) default 15000.00 check(Ordering_Shipping_Fee > 0)")
    private BigDecimal orderingShippingFee;

    @Column(name = "Ordering_Price", nullable = false, columnDefinition = "decimal(18, 2) default 0.00 check(Ordering_Price >= 0)")
    private BigDecimal orderingPrice;

    @Column(name = "Ordering_Total_Price", nullable = false, columnDefinition = "decimal(18, 2) default 0.00 check(Ordering_Total_Price >= 0)")
    private BigDecimal orderingTotalPrice;

    @Column(name = "Ordering_Note", length = 255)
    private String orderingNote;

    @Column(name = "Account_Phone", nullable = false)
    private String accountPhone;

    @ManyToOne
    @JoinColumn(name = "Discount_ID")
    private DiscountEntity discount;

	public OrderingEntity(int orderingID, int orderingStatus, LocalDateTime orderingCreationDate, BigDecimal orderingShippingFee,
			BigDecimal orderingPrice, BigDecimal orderingTotalPrice, String orderingNote, String accountPhone,
			DiscountEntity discount) {
		this.orderingID = orderingID;
		this.orderingStatus = orderingStatus;
		this.orderingCreationDate = orderingCreationDate;
		this.orderingShippingFee = orderingShippingFee;
		this.orderingPrice = orderingPrice;
		this.orderingTotalPrice = orderingTotalPrice;
		this.orderingNote = orderingNote;
		this.accountPhone = accountPhone;
		this.discount = discount;
	}

	public OrderingEntity(){}

	public int getOrderingID() {
		return orderingID;
	}

	public void setOrderingID(int orderingID) {
		this.orderingID = orderingID;
	}

	public int getOrderingStatus() {
		return orderingStatus;
	}

	public void setOrderingStatus(int orderingStatus) {
		this.orderingStatus = orderingStatus;
	}

	public LocalDateTime getOrderingCreationDate() {
		return orderingCreationDate;
	}

	public void setOrderingCreationDate(LocalDateTime orderingCreationDate) {
		this.orderingCreationDate = orderingCreationDate;
	}

	public BigDecimal getOrderingShippingFee() {
		return orderingShippingFee;
	}

	public void setOrderingShippingFee(BigDecimal orderingShippingFee) {
		this.orderingShippingFee = orderingShippingFee;
	}

	public BigDecimal getOrderingPrice() {
		return orderingPrice;
	}

	public void setOrderingPrice(BigDecimal orderingPrice) {
		this.orderingPrice = orderingPrice;
	}

	public BigDecimal getOrderingTotalPrice() {
		return orderingTotalPrice;
	}

	public void setOrderingTotalPrice(BigDecimal orderingTotalPrice) {
		this.orderingTotalPrice = orderingTotalPrice;
	}

	public String getOrderingNote() {
		return orderingNote;
	}

	public void setOrderingNote(String orderingNote) {
		this.orderingNote = orderingNote;
	}

	public String getAccountPhone() {
		return accountPhone;
	}

	public void setAccountPhone(String accountPhone) {
		this.accountPhone = accountPhone;
	}

	public DiscountEntity getDiscount() {
		return discount;
	}

	public void setDiscount(DiscountEntity discount) {
		this.discount = discount;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
}
