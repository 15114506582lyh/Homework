package com.example.homework.Domain.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.homework.DAO.CustomerLocationMapper;
import com.example.homework.Domain.entity.CustomerLocation;
import org.springframework.stereotype.Service;

@Service
public class CustomerLocationServiceImpl extends ServiceImpl<CustomerLocationMapper, CustomerLocation> implements CustomerLocationService {

}
