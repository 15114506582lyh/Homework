package com.example.homework.Domain.vo;

import lombok.Data;
//request
@Data
public class ItemListReqVO extends PaginationQuerySupportImpl {
    private String itemName;
    private String status;
}
