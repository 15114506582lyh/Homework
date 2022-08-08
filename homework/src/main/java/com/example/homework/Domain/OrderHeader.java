package com.example.homework.Domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Date;

@Data
public class OrderHeader {
    @TableId("order_id")
    private Integer id;
    @TableField("order_number")
    private String number;
    @TableField("customer_id")
    private Integer cid;
    @TableField("order_date")
    private Date date;
    @TableField("status")
    private String status;
}
