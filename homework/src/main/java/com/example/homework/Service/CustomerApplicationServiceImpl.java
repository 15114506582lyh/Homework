package com.example.homework.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.example.homework.Domain.entity.Customer;
import com.example.homework.Domain.entity.CustomerLocation;
import com.example.homework.Domain.service.CustomerLocationService;
import com.example.homework.Domain.service.CustomerService;
import com.example.homework.Domain.vo.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerApplicationServiceImpl implements CustomerApplicationService {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerLocationService customerLocationService;

    @Override
//    客户信息列表查询，支持分页查询
    public CustomerListResVO list(CustomerListReqVO customerListReqVO) {
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtil.isNotEmpty(customerListReqVO.getCustomerNumber()), Customer::getCustomerNumber, customerListReqVO.getCustomerNumber());
        queryWrapper.like(StringUtil.isNotEmpty(customerListReqVO.getCustomerName()), Customer::getCustomerName, customerListReqVO.getCustomerName());
        queryWrapper.eq(StringUtil.isNotEmpty(customerListReqVO.getStatus()), Customer::getStatus, customerListReqVO.getStatus());
        Page<Customer> page = PageHelper.startPage(customerListReqVO.getPage(), customerListReqVO.getPageSize());
        List<Customer> list = customerService.list(queryWrapper);
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

    @Override
//    查询单个客户信息
    public CustomerInfoResVO list(Integer id) {
        LambdaQueryWrapper<CustomerLocation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), CustomerLocation::getCustomerId, id);
        List<CustomerLocation> list = customerLocationService.list(queryWrapper);
        CustomerInfoResVO response = new CustomerInfoResVO();
        Customer customer = customerService.getById(id);
        if (!CollectionUtils.isEmpty(list)) {
            response.setCustomerId(id);
            response.setCustomerNumber(customer.getCustomerNumber());
            response.setCustomerName(customer.getCustomerName());
            response.setCustomerType(customer.getCustomerType());
            response.setEmail(customer.getEmail());
            response.setStatus(customer.getStatus());
            response.setLocations(list);
        }
        return response;

    }

    @Override
    public CustomerSaveStringVO save(CustomerInfoResVO customerInfoResVO) {
        // 先处理头信息
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerInfoResVO, customer);
        CustomerSaveStringVO customerSaveStringVO=new CustomerSaveStringVO();
        if(customerService.getById(customer.getCustomerId()).getStatus().equals("有效")) {
            customerService.saveOrUpdate(customer);

            // 处理地点信息
            Optional.ofNullable(customerInfoResVO.getLocations()).orElse(new ArrayList<>()).forEach(e -> {
                e.setCustomerId(customer.getCustomerId());
            });
            customerLocationService.saveOrUpdateBatch(customerInfoResVO.getLocations());
            customerSaveStringVO.setInfo("操作成功");
        }else
            customerSaveStringVO.setInfo("操作失败");
        return customerSaveStringVO;
    }

    @Override
    public CustomerDisableStringVO disable(CustomerDisableReqVO customerDisableReqVO) {
        return null;
    }
}
