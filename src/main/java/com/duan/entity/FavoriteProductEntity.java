package com.duan.entity;

import java.io.Serializable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Favorite_Product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteProductEntity implements Serializable{
	private static final long serialVersionUID = 1L;

    @EmbeddedId
    private FavoriteProductId favoriteProductId;

    @ManyToOne
    @MapsId("Product_ID")
    @JoinColumn(name = "Product_ID")
    private ProductEntity productEntity;

    @ManyToOne
    @MapsId("Account_Phone")
    @JoinColumn(name = "Account_Phone")
    private AccountEntity accountEntity;
}
