package com.example.homework.Domain.vo;

import lombok.Data;

import java.util.Date;

@Data
public class OrderListReqVO {
    private String cuetomerName;
    private String orderNumber;
    private Date orderDate;
}
