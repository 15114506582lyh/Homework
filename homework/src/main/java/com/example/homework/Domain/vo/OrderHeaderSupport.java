package com.example.homework.Domain.vo;

import lombok.Data;

@Data
public class OrderHeaderSupport {
    private Integer orderId;
    private String orderNumber;
    private Integer customerId;
    private String customerName;
}
