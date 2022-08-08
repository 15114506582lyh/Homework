package com.example.homework.Domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Item {
    @TableId("item_id")
    private Integer id;
    @TableField("item_name")
    private String name;
    @TableField("uom")
    private String uom;
    @TableField("price")
    private Double price;
    @TableField("status")
    private String statue;
}
