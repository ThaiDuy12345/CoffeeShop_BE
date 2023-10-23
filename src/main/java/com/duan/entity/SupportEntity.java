package com.duan.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Support")
@Data
public class SupportEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Support_ID")
	private int supportID;

	@Column(name = "Support_Reason", nullable = false)
	private String supportReason;

	@Column(name = "Support_Title", nullable = false)
	private String supportTitle;

	@Column(name = "Support_Content", nullable = false)
	private String supportContent;

	@Column(name = "Support_Creation_date", nullable = false, columnDefinition = "datetime2 default CURRENT_TIMESTAMP")
	private LocalDateTime supportCreationDate;

	@Column(name = "Support_Status", nullable = false)
	private boolean supportStatus;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Account_Phone", referencedColumnName = "Account_Phone", nullable = false)
	private AccountEntity account;

	public SupportEntity(int supportID, String supportReason, String supportTitle, String supportContent,
			LocalDateTime supportCreationDate, boolean supportStatus, AccountEntity account) {
		this.supportID = supportID;
		this.supportReason = supportReason;
		this.supportTitle = supportTitle;
		this.supportContent = supportContent;
		this.supportCreationDate = supportCreationDate;
		this.supportStatus = supportStatus;
		this.account = account;
	}

	public SupportEntity() {
	}

	public int getSupportID() {
		return supportID;
	}

	public void setSupportID(int supportID) {
		this.supportID = supportID;
	}

	public String getSupportReason() {
		return supportReason;
	}

	public void setSupportReason(String supportReason) {
		this.supportReason = supportReason;
	}

	public String getSupportTitle() {
		return supportTitle;
	}

	public void setSupportTitle(String supportTitle) {
		this.supportTitle = supportTitle;
	}

	public String getSupportContent() {
		return supportContent;
	}

	public void setSupportContent(String supportContent) {
		this.supportContent = supportContent;
	}

	public LocalDateTime getSupportCreationDate() {
		return supportCreationDate;
	}

	public void setSupportCreationDate(LocalDateTime supportCreationDate) {
		this.supportCreationDate = supportCreationDate;
	}

	public boolean isSupportStatus() {
		return supportStatus;
	}

	public void setSupportStatus(boolean supportStatus) {
		this.supportStatus = supportStatus;
	}

	public AccountEntity getAccount() {
		return account;
	}

	public void setAccount(AccountEntity account) {
		this.account = account;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}