package com.example.homework.Domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.homework.Domain.entity.CustomerLocation;
import com.example.homework.Domain.vo.CustomerIdReqVO;
import com.example.homework.Domain.vo.InfoVO;
import com.example.homework.Domain.vo.LocationIdReqVO;
import com.example.homework.Domain.vo.LocationSelectorResVO;

import java.util.List;

public interface CustomerLocationService extends IService<CustomerLocation> {
    InfoVO locationDelete(LocationIdReqVO locationIdReqVO); // 删除客户收货地点
    List<LocationSelectorResVO> select(CustomerIdReqVO customerIdReqVO); // 客户地点选择器，查询单个客户下面的收货地点
}
