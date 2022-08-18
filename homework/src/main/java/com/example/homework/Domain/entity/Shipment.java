package com.example.homework.Domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@TableName("shipment")
public class Shipment {
    @TableId(value = "shipment_id",type= IdType.AUTO)
    private Integer shipmentId;
    @TableField("line_id")
    private Integer lineId;
    @TableField("address")
    private String address;
    @TableField("phone")
    private String phone;
    @TableField("estimated_shipment_date")
    private Date estimatedShipmentDate;
    @TableField("actual_shipment_date")
    private Date actualShipmentDate;
    @TableField("quantity")
    private Double quantity;
    @TableField("status")
    private BigDecimal status;
}
