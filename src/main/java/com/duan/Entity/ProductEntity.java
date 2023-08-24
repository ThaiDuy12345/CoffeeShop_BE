package com.duan.Entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "[Products]")
@Data
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Product_ID")
    private int productId;

    @Column(name = "Product_image", nullable = false)
    private String productImage;

    @Column(name = "Product_Name", nullable = false)
    private String productName;

    @Column(name = "Product_Price", nullable = false)
    private BigDecimal productPrice;

    @Column(name = "Product_Description", nullable = false)
    private String productDescription;

    @ManyToOne
    @JoinColumn(name = "ProductCategory_ID")
    private CategoryEntity productCategory;
}
