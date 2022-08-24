package com.example.homework.Domain.vo;

import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
public class ItemCreateReqVO {
    @NotBlank(message = "商品名称不能为空")
    private String itemName;
    @NotBlank(message = "商品状态不能为空")
    private String uom;
    @Digits(integer = 6,fraction = 4)
    private BigDecimal price;

}
