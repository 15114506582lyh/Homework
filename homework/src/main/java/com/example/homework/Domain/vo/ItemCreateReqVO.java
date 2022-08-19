package com.example.homework.Domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemCreateReqVO {
    private String itemName;
    private String uom;
    private BigDecimal price;

}
