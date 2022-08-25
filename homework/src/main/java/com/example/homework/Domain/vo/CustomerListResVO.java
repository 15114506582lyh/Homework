package com.example.homework.Domain.vo;

import com.example.homework.Domain.entity.Customer;
import lombok.Data;

import java.util.List;

@Data
public class CustomerListResVO extends PaginationResultSupport {
    private String info;
    List<Customer> rows;
}
