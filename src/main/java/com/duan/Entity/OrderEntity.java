package com.duan.Entity;

import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.*;

import lombok.Data;



@Entity
@Table(name = "[Order]")

public class OrderEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Order_ID")
    private int orderId;

    @Column(name = "Order_Status", nullable = false)
    private int orderStatus;

    @Column(name = "Order_Date", nullable = false)
    private Date orderDate;

    @Column(name = "Total_Price", nullable = false)
    private BigDecimal totalPrice;

    @ManyToOne
    @JoinColumn(name = "AccountOrder_ID")
    private AccountEntity accountOrder;
}
