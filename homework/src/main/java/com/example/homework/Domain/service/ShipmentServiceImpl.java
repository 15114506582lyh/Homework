package com.example.homework.Domain.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.homework.DAO.ShipmentMapper;
import com.example.homework.Domain.dto.ShipmentListDTO;
import com.example.homework.Domain.entity.Shipment;
import com.example.homework.Domain.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ShipmentServiceImpl extends ServiceImpl<ShipmentMapper, Shipment> implements ShipmentService {
    @Autowired
    ShipmentService shipmentService;

    /**
     * 某个订单行下的发货行查询接口
     *
     * @param shipmentLineIdReqVO
     * @return
     */
    @Override
    public ShipmentListResVO shipmentList(ShipmentLineIdReqVO shipmentLineIdReqVO) {
        LambdaQueryWrapper<Shipment> wrapper = Wrappers.lambdaQuery(Shipment.class).
                eq(ObjectUtils.isNotEmpty(shipmentLineIdReqVO.getLineId()),Shipment::getLineId,shipmentLineIdReqVO.getLineId());
        List<Shipment> shipments = shipmentService.list(wrapper);
        List<ShipmentListDTO> list = new ArrayList<>();
        ShipmentListResVO shipmentListResVO = new ShipmentListResVO();
        Optional.ofNullable(shipments).orElse(new ArrayList<>()).forEach(line -> {
            ShipmentListDTO shipmentListDTO = new ShipmentListDTO();
            BeanUtils.copyProperties(line, shipmentListDTO);
            list.add(shipmentListDTO);
        });
        if (!CollectionUtils.isEmpty(list))
            shipmentListResVO.setLines(list);
        return shipmentListResVO;
    }


}
