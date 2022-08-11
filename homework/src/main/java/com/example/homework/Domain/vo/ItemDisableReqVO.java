package com.example.homework.Domain.vo;

import lombok.Data;

@Data
public class ItemDisableReqVO {
    private Integer itemId;
    private String status="已下架";
}
