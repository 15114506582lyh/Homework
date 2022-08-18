package com.example.homework.Domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShipmentListDTO {
    private Integer shipmentId;
    private String address;
    private String phone;
    private String estimatedShipmentDate;
    private String actualShipmentDate;
    private BigDecimal quantity;
    private String status;
}
