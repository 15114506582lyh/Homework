package com.example.homework.Domain.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class OrderDetailDTO {
    private Integer lineId;
    private Integer itemId;
    private BigDecimal price;
    private Double quantity;
}
