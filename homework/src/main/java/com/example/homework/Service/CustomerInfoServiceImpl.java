package com.example.homework.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.homework.DAO.CustomerBaseMapper;
import com.example.homework.DAO.CustomerInfoMapper;
import com.example.homework.Domain.entity.Customer;
import com.example.homework.Domain.entity.CustomerLocation;
import com.example.homework.Domain.vo.CustomerInfoResVO;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
@Service
public class CustomerInfoServiceImpl extends ServiceImpl<CustomerInfoMapper, CustomerLocation> implements CustomerInfoService{
    @Autowired
    CustomerInfoMapper customerInfoMapper;
    @Autowired
    CustomerBaseMapper customerBaseMapper;
    @Override
    public CustomerInfoResVO list(Integer id) {
        LambdaQueryWrapper<CustomerLocation> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.eq(ObjectUtils.isNotEmpty(id),CustomerLocation::getCustomerId,id);
        List<CustomerLocation> list = customerInfoMapper.selectList(queryWrapper);
        CustomerInfoResVO response = new CustomerInfoResVO();
        Customer customer = customerBaseMapper.selectById(id);
//        if (!CollectionUtils.isEmpty(list)) {
            response.setCustomerId(id);
            response.setCustomerNumber(customer.getCustomerNumber());
            response.setCustomerName(customer.getCustomerName());
            response.setCustomerType(customer.getCustomerType());
            response.setEmail(customer.getEmail());
            response.setStatus(customer.getStatus());
            response.setRows(list);
//        }
        return response;
    }
}
