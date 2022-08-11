package com.example.homework.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.homework.Domain.entity.Customer;
import com.example.homework.Domain.vo.CustomerListReqVO;
import com.example.homework.Domain.vo.CustomerListResVO;
import com.example.homework.Domain.vo.ItemListResVO;

public interface CustomerService extends IService<Customer> {
    CustomerListResVO List(CustomerListReqVO customerListReqVO);
}
