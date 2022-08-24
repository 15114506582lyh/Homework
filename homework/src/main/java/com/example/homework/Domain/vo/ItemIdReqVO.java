package com.example.homework.Domain.vo;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class ItemIdReqVO {
    @NotNull(message = "商品id不能为空")
    private Integer itemId;
}
