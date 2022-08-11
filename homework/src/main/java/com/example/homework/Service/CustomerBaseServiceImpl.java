package com.example.homework.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.homework.DAO.CustomerMapper;
import com.example.homework.Domain.entity.Customer;
import com.example.homework.Domain.vo.CustomerListReqVO;
import com.example.homework.Domain.vo.CustomerListResVO;
import com.example.homework.Domain.vo.ItemListResVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {
    @Autowired
    private CustomerMapper customerMapper;
    @Override
    public CustomerListResVO List(CustomerListReqVO customerListReqVO) {
        LambdaQueryWrapper<Customer> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtil.isNotEmpty(customerListReqVO.getCustomerNumber()),Customer::getCustomerNumber,customerListReqVO.getCustomerNumber());
        queryWrapper.like(StringUtil.isNotEmpty(customerListReqVO.getCustomerName()),Customer::getCustomerName,customerListReqVO.getCustomerName());
        queryWrapper.eq(StringUtil.isNotEmpty(customerListReqVO.getStatus()),Customer::getStatus,customerListReqVO.getStatus());
        Page<Customer> page= PageHelper.startPage(customerListReqVO.getPage(),customerListReqVO.getPageSize());
        List<Customer> list= customerMapper.selectList(queryWrapper);
        CustomerListResVO response = new CustomerListResVO();
        if (!CollectionUtils.isEmpty(list)) {
            //返回查询总条数
            response.setTotal(page.getTotal());
            //返回总页数
            response.setTotalPages(page.getPages());
            //返回查询结果
            response.setRows(list);
        }
        return response;
    }
}
