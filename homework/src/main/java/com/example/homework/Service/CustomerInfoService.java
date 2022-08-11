package com.example.homework.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.homework.Domain.entity.Customer;
import com.example.homework.Domain.entity.CustomerLocation;
import com.example.homework.Domain.vo.CustomerInfoResVO;
import org.springframework.stereotype.Service;

public interface CustomerInfoService extends IService<CustomerLocation> {
    CustomerInfoResVO list(Integer id);
}
