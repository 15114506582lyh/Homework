package com.example.homework.Domain.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.homework.DAO.CustomerLocationMapper;
import com.example.homework.Domain.dto.LocationSelectorDTO;
import com.example.homework.Domain.entity.Customer;
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



    //

    /**
     * 删除客户收货地点
     * @param locationIdReqVO
     * @return
     */
    @Override
    public InfoVO locationDelete(LocationIdReqVO locationIdReqVO) {
        InfoVO infoVO = new InfoVO();
        if (removeById(locationIdReqVO.getLocationId())) {
            infoVO.setInfo("删除成功");
        } else
            infoVO.setInfo("删除失败");
        return infoVO;
    }



//

    /**
     * 客户地点选择器，查询单个客户下面的收货地点
     * @param customerIdReqVO
     * @return
     */

    @Override
    public LocationSelectorResVO select(CustomerIdReqVO customerIdReqVO) {
        LambdaQueryWrapper wrapper = Wrappers.lambdaQuery(CustomerLocation.class).
                eq(ObjectUtils.isNotEmpty(customerIdReqVO.getCustomerId()),CustomerLocation::getCustomerId,customerIdReqVO.getCustomerId());
        List<CustomerLocation> list = customerLocationService.list(wrapper);
        List<LocationSelectorDTO> locationSelectorDTOS = new ArrayList<>();
        LocationSelectorResVO locationSelectorResVO = new LocationSelectorResVO();
        if (ObjectUtils.isNotEmpty(list)) {
            LocationSelectorDTO locationSelectorDTO = new LocationSelectorDTO();
            for (CustomerLocation customerLocation : list) {
                BeanUtils.copyProperties(customerLocation, locationSelectorDTO);
                locationSelectorDTOS.add(locationSelectorDTO);
            }
            locationSelectorResVO.setLocations(locationSelectorDTOS);
            locationSelectorResVO.setInfo("查找成功");
        }else {
            locationSelectorResVO.setInfo("找不到该顾客");
        }
        return locationSelectorResVO;
    }
}
