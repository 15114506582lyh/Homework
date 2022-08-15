package com.example.homework.Domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.homework.DAO.CustomerLocationMapper;
import com.example.homework.Domain.entity.CustomerLocation;
import com.example.homework.Domain.entity.OrderHeader;
import com.example.homework.Domain.vo.CustomerIdReqVO;
import com.example.homework.Domain.vo.InfoVO;
import com.example.homework.Domain.vo.LocationIdReqVO;
import com.example.homework.Domain.vo.LocationSelectorResVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CustomerLocationServiceImpl extends ServiceImpl<CustomerLocationMapper, CustomerLocation> implements CustomerLocationService {
    @Autowired
    private CustomerLocationService customerLocationService;



    //    删除客户收货地点
    @Override
    public InfoVO locationDelete(LocationIdReqVO locationIdReqVO) {
        InfoVO infoVO = new InfoVO();
        if (removeById(locationIdReqVO.getLocationId())) {
            infoVO.setInfo("删除成功");
        } else
            infoVO.setInfo("删除失败");
        return infoVO;
    }



//    客户地点选择器，查询单个客户下面的收货地点

    @Override
    public List<LocationSelectorResVO> select(CustomerIdReqVO customerIdReqVO) {
        QueryWrapper<CustomerLocation> wrapper = new QueryWrapper<>();
        wrapper.eq("customer_id",customerIdReqVO.getCustomerId());
        List<CustomerLocation> list = customerLocationService.list(wrapper);
        List<LocationSelectorResVO> list1 = list.stream().map(location->{
            LocationSelectorResVO locationSelectorResVO = new LocationSelectorResVO();
            locationSelectorResVO.setLocationId(location.getLocationId());
            locationSelectorResVO.setAddress(location.getAddress());
            locationSelectorResVO.setPhone(location.getPhone());
            return locationSelectorResVO;
        }).collect(Collectors.toList());
        return list1;
    }
}
