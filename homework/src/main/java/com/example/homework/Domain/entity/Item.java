package com.example.homework.Domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "item")
public class Item {
    @TableId("item_id")
    private Integer itemId;
    @TableField("item_name")
    private String itemName;
    @TableField("uom")
    private String uom;
    @TableField("price")
    private Double price;
    @TableField("status")
    private String status;
}
