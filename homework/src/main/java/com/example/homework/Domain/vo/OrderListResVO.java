package com.example.homework.Domain.vo;

import lombok.Data;

import java.util.Date;

@Data
public class OrderListResVO extends PriceSupport{
    private Integer number;
    private String customerName;
    private String orderNumber;
    private Date orderDate;
    private String itemName;
    private String status;
}
