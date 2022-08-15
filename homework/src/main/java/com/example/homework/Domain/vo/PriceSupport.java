package com.example.homework.Domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PriceSupport extends PaginationResultSupport{
    private BigDecimal totalPrice=BigDecimal.ZERO;
}
