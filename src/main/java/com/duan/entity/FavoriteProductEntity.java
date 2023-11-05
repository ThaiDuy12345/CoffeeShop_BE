package com.duan.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Favorite_Product")
@Data
public class FavoriteProductEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
    @Column(name = "Account_Phone", length = 12, nullable = false)
    private String accountPhone;

    @Id
    @Column(name = "Product_ID", nullable = false)
    private int productId;

    @ManyToOne
    @JoinColumn(name = "Account_Phone")
    private AccountEntity accountEntity;

    @ManyToOne
    @JoinColumn(name = "Product_ID")
    private ProductEntity productEntity;
}
