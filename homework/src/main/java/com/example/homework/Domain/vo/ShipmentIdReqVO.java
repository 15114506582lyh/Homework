package com.example.homework.Domain.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ShipmentIdReqVO {
    @NotNull(message = "发货行id不能为空")
    private Integer shipmentId;
}
