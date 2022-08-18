package com.example.homework.Domain.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.homework.DAO.OrderHeaderMapper;
import com.example.homework.Domain.entity.OrderHeader;
import com.example.homework.Domain.vo.InfoVO;
import com.example.homework.Domain.vo.OrderDetailReqVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderHeaderServiceImpl extends ServiceImpl<OrderHeaderMapper, OrderHeader> implements OrderHeaderService{
}
