package com.example.homework.Domain.vo;

import com.example.homework.Domain.dto.OrderDetailDTO;
import com.example.homework.Domain.entity.OrderHeader;
import lombok.Data;
import java.util.List;

@Data
public class OrderDetailResVO extends OrderHeaderSupport {
    List<OrderDetailDTO> lines;
}
