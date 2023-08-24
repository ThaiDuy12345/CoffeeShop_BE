package com.duan.Entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Detail_Order")
@Data
public class DetailOrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Detail_Order_ID")
    private int detailOrderId;

    @Column(name = "Quantity", nullable = false)
    private int quantity;

    @Column(name = "Sub_Total", nullable = false)
    private BigDecimal subTotal;

    @ManyToOne
    @JoinColumn(name = "FK_Order_ID")
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "FK_Product_ID")
    private ProductEntity product;
}
