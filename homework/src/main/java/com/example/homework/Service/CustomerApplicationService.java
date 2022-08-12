package com.example.homework.Service;

import com.example.homework.Domain.vo.*;

public interface CustomerApplicationService {
    CustomerListResVO list(CustomerListReqVO customerListReqVO);// 客户信息列表查询，支持分页查询
    CustomerInfoResVO list(Integer id);// 查询单个客户信息
    CustomerSaveStringVO save(CustomerInfoResVO customerInfoResVO);// 保存客户信息，包含收货地点信息一起保存
    CustomerDisableStringVO disable(CustomerDisableReqVO customerDisableReqVO);// 失效客户信息
}
