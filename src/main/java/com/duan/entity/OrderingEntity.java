package com.duan.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "Ordering")
@Getter
@Setter
@AllArgsConstructor
public class OrderingEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Ordering_ID")
    private int orderingID;

    @Column(name = "Ordering_Status", nullable = false, columnDefinition = "int default 0 check(Ordering_Status in (0, 1, 2, 3, 4, -1))")
    private int orderingStatus;

    @Column(name = "Ordering_Creation_Date", nullable = false, columnDefinition = "datetime2 default(GETDATE())")
    private Date orderingCreationDate;

    @Column(name = "Ordering_Shipping_Fee", nullable = false, columnDefinition = "decimal(18, 2) default 15000.00 check(Ordering_Shipping_Fee > 0)")
    private BigDecimal orderingShippingFee;

    @Column(name = "Ordering_Price", nullable = false, columnDefinition = "decimal(18, 2) default 0.00 check(Ordering_Price >= 0)")
    private BigDecimal orderingPrice;

    @Column(name = "Ordering_Total_Price", nullable = false, columnDefinition = "decimal(18, 2) default 0.00 check(Ordering_Total_Price >= 0)")
    private BigDecimal orderingTotalPrice;

    @Column(name = "Ordering_Note", length = 255)
    private String orderingNote;

    @Column(name = "Ordering_Cancel_Description", length = 255)
    private String orderingCancelDescription;

    @Column(name = "Ordering_Approve_Description", length = 255)
    private String orderingApproveDescription;

    @Column(name = "Ordering_Payment_Status", columnDefinition = "default(null) check(Ordering_Payment_Status in (null, 0, 1, -1))")
    private Integer orderingPaymentStatus;

    @ManyToOne
    @JoinColumn(name = "Account_Phone")
    private AccountEntity accountEntity;

    @ManyToOne
    @JoinColumn(name = "Updated_By")
    private AccountEntity updatedByAccountEntity;
    
    @ManyToOne
    @JoinColumn(name = "Discount_ID")
    private DiscountEntity discountEntity;

    @JsonIgnore
    @OneToMany(mappedBy = "orderingEntity")
    private List<DetailOrderEntity> detailOrderEntities;

    public OrderingEntity(){
        this.orderingCreationDate = new Date();
        this.orderingStatus = 0;
        this.orderingShippingFee = new BigDecimal(15000);
        this.orderingPaymentStatus = null;
        this.orderingPrice = new BigDecimal(0);
        this.orderingTotalPrice =  new BigDecimal(0);
    }
}
