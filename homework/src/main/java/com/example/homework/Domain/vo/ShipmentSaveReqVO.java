package com.example.homework.Domain.vo;

import com.example.homework.Domain.dto.ShipmentSaveDTO;
import com.example.homework.Domain.entity.Shipment;
import lombok.Data;

import java.util.List;
@Data
public class ShipmentSaveReqVO {
    List<ShipmentSaveDTO> lines;
}
