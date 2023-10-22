package com.duan.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Detail_Order")
@Data
public class DetailOrderEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Order_ID")
    private int orderId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Product_Size_ID")
    private int productSizeId;

    @Column(name = "Detail_Order_Product_Quantity", nullable = false, columnDefinition = "int default 1")
    private int detailOrderProductQuantity;

    @Column(name = "Detail_Order_Sub_Total", nullable = false, columnDefinition = "decimal(18, 2) default 0")
    private BigDecimal detailOrderSubTotal;

	public DetailOrderEntity(int orderId, int productSizeId, int detailOrderProductQuantity,
			BigDecimal detailOrderSubTotal) {
		super();
		this.orderId = orderId;
		this.productSizeId = productSizeId;
		this.detailOrderProductQuantity = detailOrderProductQuantity;
		this.detailOrderSubTotal = detailOrderSubTotal;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getProductSizeId() {
		return productSizeId;
	}

	public void setProductSizeId(int productSizeId) {
		this.productSizeId = productSizeId;
	}

	public int getDetailOrderProductQuantity() {
		return detailOrderProductQuantity;
	}

	public void setDetailOrderProductQuantity(int detailOrderProductQuantity) {
		this.detailOrderProductQuantity = detailOrderProductQuantity;
	}

	public BigDecimal getDetailOrderSubTotal() {
		return detailOrderSubTotal;
	}

	public void setDetailOrderSubTotal(BigDecimal detailOrderSubTotal) {
		this.detailOrderSubTotal = detailOrderSubTotal;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}