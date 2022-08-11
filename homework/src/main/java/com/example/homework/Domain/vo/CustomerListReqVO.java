package com.example.homework.Domain.vo;

import lombok.Data;

@Data
public class CustomerListReqVO extends PaginationQuerySupport {
    private String customerNumber;
    private String customerName;
    private String status;
}
