package com.example.homework.Domain.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CustomerIdReqVO {
    @NotNull(message = "客户id不能为空")
    private Integer customerId;
}
