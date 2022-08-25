package com.example.homework.Domain.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OrderIdReqVO {
    @NotNull(message = "订单头id不能为空")
    private Integer orderId;
}
