package com.duan.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Product_Size")
@Data
public class ProductSizeEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Product_Size_ID")
    private int productSizeId;

    @Column(name = "Product_Size", nullable = false, length = 1)
    @Pattern(regexp = "[SML]", message = "Product size must be S, M or L")
    private String productSize;

    @Column(name = "Product_Size_Price", nullable = false, precision = 18, scale = 2)
    @DecimalMin(value = "0.00", inclusive = true, message = "Product size price must be greater than or equal to 0")
    private BigDecimal productSizePrice;

    @ManyToOne
    @JoinColumn(name = "Product_ID", referencedColumnName = "Product_ID", nullable = false)
    private ProductEntity productEntity;

    @JsonIgnore
    @OneToMany(mappedBy = "productSizeEntity")
    private List<DetailOrderEntity> detailOrderEntities;
}
