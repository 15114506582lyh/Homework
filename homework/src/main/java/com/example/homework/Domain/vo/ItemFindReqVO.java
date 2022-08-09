package com.example.homework.Domain.vo;

import lombok.Data;

@Data
public class ItemFindReqVO extends PaginationQuerySupportImpl {
    private String itemName;
    private String status;
}
