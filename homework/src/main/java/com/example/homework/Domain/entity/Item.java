package com.example.homework.Domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@TableName("item")
public class Item {
    @TableId(value="item_id",type= IdType.AUTO)
    @NotNull(message = "商品id不能为空")
    private Integer itemId;
    @TableField("item_name")
    private String itemName;
    @TableField("uom")
    private String uom;
    @TableField(value = "price")
    @Digits(integer = 6,fraction = 4)
    private BigDecimal price;
    @TableField("status")
    private String status;
}
