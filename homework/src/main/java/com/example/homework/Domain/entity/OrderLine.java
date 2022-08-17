package com.example.homework.Domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("orderline")
public class OrderLine {
    @TableId(value = "line_id",type= IdType.AUTO)
    private Integer lineId;
    @TableField("order_id")
    private Integer orderId;
    @TableField("item_id")
    private Integer itemId;
    @TableField("price")
    private BigDecimal price;
    @TableField("quantity")
    private BigDecimal quantity;
}
