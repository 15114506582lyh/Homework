package com.example.homework.Domain.vo;

import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Data
public class ItemAddReqVO {

    @NotBlank(message = "商品名称不能为空")
    private String itemName;

    private String uom;

    @Digits(integer = 6, fraction = 4, message = "参考单价数据长度不合法")
//    @Pattern(regexp = "^\\d.", message = "不满足参考单价格式")
    private BigDecimal price;

}
