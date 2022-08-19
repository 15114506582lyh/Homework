package com.example.homework.Domain.vo;

import com.example.homework.Domain.dto.CustomerDetailDTO;
import com.example.homework.Domain.entity.Customer;
import com.example.homework.Domain.entity.CustomerLocation;
import lombok.Data;

import java.util.List;
@Data
public class CustomerDetailResVO extends Customer {
    List<CustomerDetailDTO> locations;
}
