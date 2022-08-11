package com.example.homework.Domain.vo;

import lombok.Data;
//request
@Data
public class ItemListReqVO extends PaginationQuerySupport {
    private String itemName;
    private String status;
}
