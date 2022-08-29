package com.example.homework.Domain.vo;

import com.example.homework.Domain.dto.OrderLineDTO;
import lombok.Data;
import java.util.List;

@Data
public class OrderDetailResVO extends OrderHeaderSupport {
    List<OrderLineDTO> lines;
}
