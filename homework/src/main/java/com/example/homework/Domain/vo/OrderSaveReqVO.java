package com.example.homework.Domain.vo;
import com.example.homework.Domain.dto.OrderLineDTO;
import com.example.homework.Domain.entity.OrderHeader;
import lombok.Data;

import java.util.List;
@Data
public class OrderSaveReqVO extends OrderHeader {
    List<OrderLineDTO> lines;
}
