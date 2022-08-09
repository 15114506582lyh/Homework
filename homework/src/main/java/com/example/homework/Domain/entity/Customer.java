package com.example.homework.Domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
@Data
public class Customer {
    @TableId("customer_id")
    private Integer id;
    @TableField("customer_number")
    private String number;
    @TableField("customer_name")
    private String name;
    @TableField("customer_type")
    private String type;
    @TableField("email")
    private String email;
    @TableField("status")
    private String status;
}
