package com.example.homework.Domain.vo;

import com.example.homework.Domain.dto.ShipmentListDTO;
import lombok.Data;

import java.util.List;

@Data
public class ShipmentListResVO{
    private List<ShipmentListDTO> lines;
}
