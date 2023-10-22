package com.duan.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Feedback")
@Data
public class FeedbackEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @ManyToOne
    @JoinColumn(name = "Product_ID")
    private ProductEntity product;

    @Id
    @ManyToOne
    @JoinColumn(name = "Account_Phone")
    private AccountEntity account;

    @Column(name = "Feedback_Rate", nullable = false, columnDefinition = "int check(Feedback_Rate in (1, 2, 3, 4, 5))")
    private int feedbackRate;

    @Column(name = "Feedback_Comment", nullable = false, length = 255)
    private String feedbackComment;

    @Column(name = "Feedback_Creation_Date", nullable = false, columnDefinition = "datetime2 default(GETDATE())")
    private Date feedbackCreationDate;

    @Column(name = "Feedback_Active", nullable = false, columnDefinition = "bit default(1)")
    private boolean feedbackActive;

	public FeedbackEntity(ProductEntity product, AccountEntity account, int feedbackRate, String feedbackComment,
			Date feedbackCreationDate, boolean feedbackActive) {
		super();
		this.product = product;
		this.account = account;
		this.feedbackRate = feedbackRate;
		this.feedbackComment = feedbackComment;
		this.feedbackCreationDate = feedbackCreationDate;
		this.feedbackActive = feedbackActive;
	}

	public ProductEntity getProduct() {
		return product;
	}

	public void setProduct(ProductEntity product) {
		this.product = product;
	}

	public AccountEntity getAccount() {
		return account;
	}

	public void setAccount(AccountEntity account) {
		this.account = account;
	}

	public int getFeedbackRate() {
		return feedbackRate;
	}

	public void setFeedbackRate(int feedbackRate) {
		this.feedbackRate = feedbackRate;
	}

	public String getFeedbackComment() {
		return feedbackComment;
	}

	public void setFeedbackComment(String feedbackComment) {
		this.feedbackComment = feedbackComment;
	}

	public Date getFeedbackCreationDate() {
		return feedbackCreationDate;
	}

	public void setFeedbackCreationDate(Date feedbackCreationDate) {
		this.feedbackCreationDate = feedbackCreationDate;
	}

	public boolean isFeedbackActive() {
		return feedbackActive;
	}

	public void setFeedbackActive(boolean feedbackActive) {
		this.feedbackActive = feedbackActive;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}