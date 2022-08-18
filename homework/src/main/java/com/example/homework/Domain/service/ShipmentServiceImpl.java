package com.example.homework.Domain.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.homework.DAO.ShipmentMapper;
import com.example.homework.Domain.entity.Shipment;
import org.springframework.stereotype.Service;

@Service
public class ShipmentServiceImpl extends ServiceImpl<ShipmentMapper, Shipment> implements ShipmentService{
}
