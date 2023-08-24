package com.duan.Entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "[Category]")
@Data
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Category_ID")
    private int categoryId;

    @Column(name = "Category_Name", nullable = false)
    private String categoryName;
}
