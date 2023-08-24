package com.duan.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "Account")
@Data
public class AccountEntity {
	@Id
	 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Account_ID")
    private int accountId;

    @Column(name = "Name", nullable = false, length = 50)
    private String name;

    @Column(name = "Password", nullable = false)
    private String password;

    @Column(name = "Address", length = 255)
    private String address;

    @Column(name = "Phone", length = 13, unique = true)
    private String phone;

    @Column(name = "Email", nullable = false, unique = true)
    private String email;

    @Column(name = "Role", nullable = false)
    private boolean role;
}
