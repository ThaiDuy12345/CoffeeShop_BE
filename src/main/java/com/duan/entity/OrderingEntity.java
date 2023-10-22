package com.duan.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
    private Date orderingCreationDate;

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
    
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "Discount_ID")
    private DiscountEntity discount;
    
}
