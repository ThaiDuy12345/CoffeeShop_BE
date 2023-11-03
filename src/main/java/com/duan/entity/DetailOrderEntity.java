package com.duan.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Detail_Order")
@Data
public class DetailOrderEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
  @EmbeddedId
  private DetailOrderId detailOrderId;

  @ManyToOne
  @MapsId("orderingID")
  @JoinColumn(name = "Ordering_ID")
  private OrderingEntity orderingEntity;

  @ManyToOne
  @MapsId("productSizeId")
  @JoinColumn(name = "Product_Size_ID")
  private ProductSizeEntity productSizeEntity;

  @Column(name = "Detail_Order_Product_Quantity", nullable = false, columnDefinition = "int default 1")
  private int detailOrderProductQuantity;

  @Column(name = "Detail_Order_Sub_Total", nullable = false, columnDefinition = "decimal(18, 2) default 0")
  private BigDecimal detailOrderSubTotal;
}
