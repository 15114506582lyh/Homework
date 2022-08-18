package com.example.homework.Domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Date;

@Data
@TableName("orderheader")
public class OrderHeader {
    @TableId(value = "order_id",type= IdType.AUTO)
    private Integer orderId;
    @TableField("order_number")
    private String orderNumber;
    @TableField("customer_id")
    private Integer customerId;
    @TableField("order_date")
    private String orderDate;
    @TableField("status")
    private String status;
}
