package com.example.homework.Domain.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.homework.DAO.OrderLineMapper;
import com.example.homework.Domain.entity.OrderLine;
import org.springframework.stereotype.Service;

@Service
public class OrderLineServiceImpl extends ServiceImpl<OrderLineMapper, OrderLine> implements OrderLineService{
}
