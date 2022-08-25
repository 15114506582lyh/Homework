package com.example.homework.Domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OrderHeaderDTO {
    private Integer orderId;
    private String orderNumber;
    @NotNull(message = "客户id不能为空")
    private Integer customerId;
    private String orderDate;
    private String status;
}
