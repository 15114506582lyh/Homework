package com.example.homework.Domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class CustomerLocation {
    @TableId("location_id")
    private Integer id;
    @TableField("customer_id")
    private Integer cid;
    @TableField("address")
    private String addr;
    @TableField("phone")
    private String phone;
}
