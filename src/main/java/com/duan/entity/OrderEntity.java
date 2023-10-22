package com.duan.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
@Table(name = "Order")
@Data
public class OrderEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Order_ID")
    private int orderID;

    @Column(name = "Order_Status", nullable = false, columnDefinition = "int default 0 check(Order_Status in (0, 1, 2, 3, 4, -1))")
    private int orderStatus;

    @Column(name = "Order_Creation_Date", nullable = false, columnDefinition = "datetime2 default(GETDATE())")
    private LocalDateTime orderCreationDate;

    @Column(name = "Order_Shipping_Fee", nullable = false, columnDefinition = "decimal(18, 2) default 15000.00 check(Order_Shipping_Fee > 0)")
    private BigDecimal orderShippingFee;

    @Column(name = "Order_Price", nullable = false, columnDefinition = "decimal(18, 2) default 0.00 check(Order_Price >= 0)")
    private BigDecimal orderPrice;

    @Column(name = "Order_Total_Price", nullable = false, columnDefinition = "decimal(18, 2) default 0.00 check(Order_Total_Price >= 0)")
    private BigDecimal orderTotalPrice;

    @Column(name = "Order_Note", length = 255)
    private String orderNote;

    @Column(name = "Account_Phone", nullable = false)
    private String accountPhone;

    @ManyToOne
    @JoinColumn(name = "Discount_ID")
    private DiscountEntity discount;

	public OrderEntity(int orderID, int orderStatus, LocalDateTime orderCreationDate, BigDecimal orderShippingFee,
			BigDecimal orderPrice, BigDecimal orderTotalPrice, String orderNote, String accountPhone,
			DiscountEntity discount) {
		this.orderID = orderID;
		this.orderStatus = orderStatus;
		this.orderCreationDate = orderCreationDate;
		this.orderShippingFee = orderShippingFee;
		this.orderPrice = orderPrice;
		this.orderTotalPrice = orderTotalPrice;
		this.orderNote = orderNote;
		this.accountPhone = accountPhone;
		this.discount = discount;
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public LocalDateTime getOrderCreationDate() {
		return orderCreationDate;
	}

	public void setOrderCreationDate(LocalDateTime orderCreationDate) {
		this.orderCreationDate = orderCreationDate;
	}

	public BigDecimal getOrderShippingFee() {
		return orderShippingFee;
	}

	public void setOrderShippingFee(BigDecimal orderShippingFee) {
		this.orderShippingFee = orderShippingFee;
	}

	public BigDecimal getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(BigDecimal orderPrice) {
		this.orderPrice = orderPrice;
	}

	public BigDecimal getOrderTotalPrice() {
		return orderTotalPrice;
	}

	public void setOrderTotalPrice(BigDecimal orderTotalPrice) {
		this.orderTotalPrice = orderTotalPrice;
	}

	public String getOrderNote() {
		return orderNote;
	}

	public void setOrderNote(String orderNote) {
		this.orderNote = orderNote;
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