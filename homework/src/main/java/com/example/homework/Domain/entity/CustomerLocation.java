package com.example.homework.Domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("customerlocation")
public class CustomerLocation {
    @TableId("location_id")
    private Integer locationId;
    @TableField("customer_id")
    private Integer customerId;
    @TableField("address")
    private String address;
    @TableField("phone")
    private String phone;
}
