package com.duan.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.Data;

@Embeddable
@Data

public class FeedbackId implements Serializable{
	    @Column(name = "Product_ID")
	    int productId;

	    @Column(name = "Account_Phone")
	    String accountPhone;


}
