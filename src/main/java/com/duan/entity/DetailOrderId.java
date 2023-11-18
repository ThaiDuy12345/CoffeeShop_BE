package com.duan.entity;
import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;
import lombok.Data;

@Embeddable
@Data
public class DetailOrderId implements Serializable {
    @Column(name = "Ordering_ID")
    private int orderingId;

    @Column(name = "Product_Size_ID")
    private int productSizeId;
}
