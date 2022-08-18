package com.example.homework.Domain.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;

@Data
public class ShipmentSaveDTO {
    private Integer lineId;
    private String address;
    private String phone;
    private String estimatedShipmentDate;
    private BigDecimal quantity;
}
