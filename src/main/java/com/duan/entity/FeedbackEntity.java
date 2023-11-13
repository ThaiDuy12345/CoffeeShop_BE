package com.duan.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table(name = "Feedback")
@Data
public class FeedbackEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    FeedbackId feedbackId;
    
    @MapsId("productId")
    @ManyToOne
    @JoinColumn(name = "Product_ID")
    private ProductEntity product;

    @MapsId("accountPhone")
    @ManyToOne
    @JoinColumn(name = "Account_Phone")
    private AccountEntity account;

    @Column(name = "Feedback_Rate", nullable = false, columnDefinition = "int check(Feedback_Rate in (1, 2, 3, 4, 5))")
    private int feedbackRate;

    @Column(name = "Feedback_Comment", nullable = false)
    private String feedbackComment;

    @Column(name = "Feedback_Creation_Date", nullable = false, columnDefinition = "datetime2 default(GETDATE())")
    private Date feedbackCreationDate;

    @Column(name = "Feedback_Active", nullable = false, columnDefinition = "bit default 1")
    private boolean feedbackActive;
}

