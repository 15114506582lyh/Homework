package com.example.homework.Domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.example.homework.Domain.entity.Customer;
import com.example.homework.Domain.entity.CustomerLocation;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CustomerSaveReqVO {
    private Integer customerId;
    @NotBlank(message = "客户编码不能为空")
    private String customerNumber;
    @NotBlank(message = "客户名称不能为空")
    private String customerName;
    private String customerType;
    private String email;
    private String status;
    private List<CustomerLocation> locations;

}
