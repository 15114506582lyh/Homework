package com.example.homework.Domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
@Data
public class OrderLineDTO {
    private Integer lineId;
    private Integer orderId;
    @NotNull(message = "商品id不能为空")
    private Integer itemId;
    @NotBlank(message = "商品价格不能为空")
    private BigDecimal price;
    @NotBlank(message = "商品数量不能为空")
    private BigDecimal quantity;
}
