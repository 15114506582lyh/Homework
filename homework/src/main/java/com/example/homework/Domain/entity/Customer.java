package com.example.homework.Domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
@Data
@TableName("cuotomer")
public class Customer {
    @TableId("customer_id")
    private Integer customerId;
    @TableField("customer_number")
    private String customerNumber;
    @TableField("customer_name")
    private String customerName;
    @TableField("customer_type")
    private String customerType;
    @TableField("email")
    private String email;
    @TableField("status")
    private String status;
}
