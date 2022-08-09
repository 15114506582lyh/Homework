package com.example.homework.Domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Date;

@Data
public class Shipment {
    @TableId("shipment_id")
    private Integer id;
    @TableField("line_id")
    private Integer lid;
    @TableField("address")
    private String addr;
    @TableField("phone")
    private String phone;
    @TableField("shipment_date")
    private Date date;
    @TableField("quantity")
    private Double qua;
    @TableField("status")
    private String status;
}
