package com.duan.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "Account")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AccountModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "Account_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int accountId;
	public AccountModel(){
		accountId=0;
	}
	@Column(name = "Name")
	@Size(min = 5, max = 50, message = "Tên phải từ 5 - 50 kí tự")
	private String name;
	@Column(name = "Password")
	private String password;
	@Column(name = "Address")
	@Size(min = 5, max = 225, message = "Địa chỉ phải từ 5 - 50 kí tự")
    private String address;
	@Column(name = "Phone")
	@Size(min = 9, max = 13, message = "Số điện thoại phải từ 9 - 13 kí tự")
    private String phone;
	@Column(name = "Email")
    private String email;
	@Column(name = "Role")
    private boolean role;
	public AccountModel(int accountId, String name,String password, String address,String phone, String email,boolean role) {
		this.accountId = accountId;
		this.name = name;
		this.password = password;
		this.address = address;
		this.phone = phone;
		this.email = email;
		this.role = role;
	}
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isRole() {
		return role;
	}
	public void setRole(boolean role) {
		this.role = role;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
