package com.example.homework.Domain.vo;

import com.example.homework.Domain.dto.OrderListDTO;
import lombok.Data;

import java.util.List;

@Data
public class OrderListResVO extends PaginationResultSupport{

    List<OrderListDTO> rows;

}
