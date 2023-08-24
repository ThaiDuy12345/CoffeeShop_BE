package com.duan.Entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "FeedBack")
@Data
public class FeedbackEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FeedBack_ID")
    private int feedBackId;

    @Column(name = "Rate", nullable = false)
    private int rate;

    @Column(name = "Comment", nullable = false, length = 255)
    private String comment;

    @Column(name = "Creation_Date", nullable = false)
    private Date creationDate;

    @ManyToOne
    @JoinColumn(name = "FeedBack_Product_ID")
    private ProductEntity feedbackProduct;

    @ManyToOne
    @JoinColumn(name = "FeedBack_Account_ID")
    private AccountEntity feedbackAccount;
}
