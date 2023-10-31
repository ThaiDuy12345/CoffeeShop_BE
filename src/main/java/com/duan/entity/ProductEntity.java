package com.duan.entity;

import java.io.Serializable;
import java.util.Date;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class ProductEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Product_ID")
	// @JsonIgnore
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
    private Date productCreationDate;

    @Column(name = "Product_Image_Url", nullable = false, columnDefinition = "nvarchar(max)")
    private String productImageUrl;

    @ManyToOne
    @JoinColumn(name = "Category_ID", nullable = false)
    private CategoryEntity category;

		@OneToMany(mappedBy = "product")
		private ProductSizeEntity productSizeEntities;

    public ProductEntity() {
        this.productCreationDate = new Date();
    }
}
