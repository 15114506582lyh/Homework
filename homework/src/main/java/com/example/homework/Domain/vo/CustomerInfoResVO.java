package com.example.homework.Domain.vo;

import com.example.homework.Domain.entity.CustomerLocation;
import lombok.Data;

import java.util.List;
@Data
public class CustomerInfoResVO extends CustomerResultSupport{
    List<CustomerLocation> rows;
}
