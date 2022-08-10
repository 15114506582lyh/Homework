package com.example.homework.Domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("orederline")
public class OrderLine {
    @TableId("line_id")
    private Integer lineId;
    @TableField("order_id")
    private Integer orderId;
    @TableField("item_id")
    private Integer itemId;
    @TableField("price")
    private BigDecimal price;
    @TableField("quantity")
    private Double quantity;
}
