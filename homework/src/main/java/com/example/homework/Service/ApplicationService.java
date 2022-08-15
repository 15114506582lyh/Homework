package com.example.homework.Service;

import com.example.homework.Domain.vo.*;

public interface ApplicationService {
    CustomerListResVO list(CustomerListReqVO customerListReqVO);// 客户信息列表查询，支持分页查询
    CustomerInfoResVO list(Integer id);// 查询单个客户信息
    InfoVO save(CustomerInfoResVO customerInfoResVO);// 保存客户信息，包含收货地点信息一起保存
    InfoVO disable(CustomerIdReqVO customerIdReqVO);// 失效客户信息
}
