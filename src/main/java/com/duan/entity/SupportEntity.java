package com.duan.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    private Date supportCreationDate;

    @Column(name = "Support_Status", nullable = false)
    private boolean supportStatus;

    @ManyToOne
    @JoinColumn(name = "Account_Phone")
    private AccountEntity accountEntity;
}
