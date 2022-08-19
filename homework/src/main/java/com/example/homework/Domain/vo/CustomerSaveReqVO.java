package com.example.homework.Domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.example.homework.Domain.entity.Customer;
import com.example.homework.Domain.entity.CustomerLocation;
import lombok.Data;

import java.util.List;

@Data
public class CustomerSaveReqVO {
    private Integer customerId;
    private String customerNumber;
    private String customerName;
    private String customerType;
    private String email;
    private List<CustomerLocation> locations;

}
