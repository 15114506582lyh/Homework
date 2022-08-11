package com.example.homework.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.homework.Domain.entity.Customer;
import com.example.homework.Domain.vo.CustomerListReqVO;
import com.example.homework.Domain.vo.CustomerListResVO;
import org.springframework.stereotype.Service;

@Service
public interface CustomerBaseService extends IService<Customer> {
    CustomerListResVO list(CustomerListReqVO customerListReqVO);
}
