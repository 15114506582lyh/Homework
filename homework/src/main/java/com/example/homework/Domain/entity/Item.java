package com.example.homework.Domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("item")
public class Item {
    @TableId(value="item_id",type= IdType.AUTO)
    private Integer itemId;
    @TableField("item_name")
    private String itemName;
    @TableField("uom")
    private String uom;
    @TableField(value = "price")
    private BigDecimal price;
    @TableField("status")
    private String status;
}
