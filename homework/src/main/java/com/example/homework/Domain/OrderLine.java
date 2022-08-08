package com.example.homework.Domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class OrderLine {
    @TableId("item_id")
    private Integer id;
    @TableField("order_id")
    private Integer oid;
    @TableField("item_id")
    private Integer iid;
    @TableField("price")
    private Double price;
    @TableField("quantity")
    private Double quantity;
}
