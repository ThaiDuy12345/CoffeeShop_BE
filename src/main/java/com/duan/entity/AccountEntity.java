package com.duan.entity;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import org.hibernate.annotations.Check;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Account")
@Data
public class AccountEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Account_Phone", length = 12)
	private String accountPhone;
	
	@Column(name = "Account_Name", nullable = false, length = 255)
    private String accountName;

    @Column(name = "Account_Password", nullable = false, length = 50)
    private String accountPassword;

    @Column(name = "Account_Address", length = 255, nullable = true)
    private String accountAddress;

    @Column(name = "Account_Email", nullable = false, unique = true, length = 50)
    @Pattern(regexp = ".+@.+\\..+")
    private String accountEmail;

    @Column(name = "Account_Role", nullable = false, columnDefinition = "int default 2")
    @Min(0)
    @Max(2)
    @Check(constraints = "Account_Role IN (0, 1, 2)")
    private int accountRole;
    
    @Column(name = "Account_Active", nullable = false, columnDefinition = "bit default true")
    private boolean accountActive;

	public AccountEntity(String accountPhone, String accountName, String accountPassword, 
			String accountAddress, String accountEmail, int accountRole,
			boolean accountActive) {
		this.accountPhone = accountPhone;
		this.accountName = accountName;
		this.accountPassword = accountPassword;
		this.accountAddress = accountAddress;
		this.accountEmail = accountEmail;
		this.accountRole = accountRole;
		this.accountActive = accountActive;
	}

	public String getAccountPhone() {
		return accountPhone;
	}

	public void setAccountPhone(String accountPhone) {
		this.accountPhone = accountPhone;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountPassword() {
		return accountPassword;
	}

	public void setAccountPassword(String accountPassword) {
		this.accountPassword = accountPassword;
	}

	public String getAccountAddress() {
		return accountAddress;
	}

	public void setAccountAddress(String accountAddress) {
		this.accountAddress = accountAddress;
	}

	public String getAccountEmail() {
		return accountEmail;
	}

	public void setAccountEmail(String accountEmail) {
		this.accountEmail = accountEmail;
	}

	public int getAccountRole() {
		return accountRole;
	}

	public void setAccountRole(int accountRole) {
		this.accountRole = accountRole;
	}

	public boolean isAccountActive() {
		return accountActive;
	}

	public void setAccountActive(boolean accountActive) {
		this.accountActive = accountActive;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
}
