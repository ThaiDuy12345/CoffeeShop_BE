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
    private AccountEntity account;

    @ManyToOne
    @JoinColumn(name = "Product_ID")
    private ProductEntity product;

	public FavoriteProductEntity(String accountPhone, int productId, AccountEntity account, ProductEntity product) {
		super();
		this.accountPhone = accountPhone;
		this.productId = productId;
		this.account = account;
		this.product = product;
	}

	public String getAccountPhone() {
		return accountPhone;
	}

	public void setAccountPhone(String accountPhone) {
		this.accountPhone = accountPhone;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public AccountEntity getAccount() {
		return account;
	}

	public void setAccount(AccountEntity account) {
		this.account = account;
	}

	public ProductEntity getProduct() {
		return product;
	}

	public void setProduct(ProductEntity product) {
		this.product = product;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}