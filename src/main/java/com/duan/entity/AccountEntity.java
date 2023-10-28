package com.duan.entity;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import org.hibernate.annotations.Check;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Account")
@Data
public class AccountEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
    @Column(name = "Account_Phone", nullable = false, length = 13)
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

		@JsonIgnore
		@OneToMany(mappedBy = "account")
		private List<SupportEntity> supportEntities;
}
