package com.example.homework.Domain.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.homework.DAO.CustomerBaseMapper;
import com.example.homework.Domain.entity.Customer;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerBaseMapper, Customer> implements CustomerService {

}
