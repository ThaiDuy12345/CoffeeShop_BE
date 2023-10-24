package com.duan.entity;

import java.io.Serializable;
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
@Table(name = "Product")
@Data
public class ProductEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Product_ID")
    private int productId;

    @Column(name = "Product_Name", nullable = false)
    private String productName;

    @Column(name = "Product_Description", nullable = false, columnDefinition = "nvarchar(max)")
    private String productDescription;

    @Column(name = "Product_Is_Popular", nullable = false, columnDefinition = "bit default 0")
    private boolean productIsPopular;

    @Column(name = "Product_Active", nullable = false, columnDefinition = "bit default 1")
    private boolean productActive;

    @Column(name = "Product_Creation_Date", nullable = false, columnDefinition = "datetime2 default GETDATE()")
    private LocalDateTime productCreationDate;

    @Column(name = "Product_Image_Url", nullable = false, columnDefinition = "nvarchar(max)")
    private String productImageUrl;

    @ManyToOne
    @JoinColumn(name = "Category_ID", nullable = false)
    private CategoryEntity category;

	public ProductEntity(int productId, String productName, String productDescription, boolean productIsPopular,
			boolean productActive, LocalDateTime productCreationDate, String productImageUrl, CategoryEntity category) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.productDescription = productDescription;
		this.productIsPopular = productIsPopular;
		this.productActive = productActive;
		this.productCreationDate = productCreationDate;
		this.productImageUrl = productImageUrl;
		this.category = category;
	}
	
	

	public ProductEntity() {
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public boolean isProductIsPopular() {
		return productIsPopular;
	}

	public void setProductIsPopular(boolean productIsPopular) {
		this.productIsPopular = productIsPopular;
	}

	public boolean isProductActive() {
		return productActive;
	}

	public void setProductActive(boolean productActive) {
		this.productActive = productActive;
	}

	public LocalDateTime getProductCreationDate() {
		return productCreationDate;
	}

	public void setProductCreationDate(LocalDateTime productCreationDate) {
		this.productCreationDate = productCreationDate;
	}

	public String getProductImageUrl() {
		return productImageUrl;
	}

	public void setProductImageUrl(String productImageUrl) {
		this.productImageUrl = productImageUrl;
	}

	public CategoryEntity getCategory() {
		return category;
	}

	public void setCategory(CategoryEntity category) {
		this.category = category;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
