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
    /**
     * 订单发货行确认发货接口
     * @param shipmentIdReqVO
     * @return
     */
    @Override
    public InfoVO shipmentConfirm(ShipmentIdReqVO shipmentIdReqVO) {
        InfoVO infoVO = new InfoVO();
        Shipment shipment = shipmentService.getById(shipmentIdReqVO.getShipmentId());
        if(ObjectUtils.isNotEmpty(shipmentIdReqVO.getShipmentId())
                &&shipment.getShipmentId().equals(shipmentIdReqVO.getShipmentId())){
            if (shipment.getStatus().equals("待发货")||shipment.getStatus().equals("发货中")
                    &&StringUtils.isNotEmpty(shipment.getActualShipmentDate())) {
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " );
                shipment.setActualShipmentDate(sdf.format(date));
                shipment.setStatus("已发货");
                updateById(shipment);
                infoVO.setInfo("成功");
            }else
                infoVO.setInfo("订单状态不符");
        }else
            infoVO.setInfo("发货行为空或发货行不存在");
        return infoVO;
    }

    /**
     * 订单发货行删除接口
     * @param shipmentIdReqVO
     * @return
     */
    @Override
    public InfoVO shipmentDelete(ShipmentIdReqVO shipmentIdReqVO) {
        InfoVO infoVO = new InfoVO();
        Shipment shipment = shipmentService.getById(shipmentIdReqVO.getShipmentId());
        if (shipmentService.removeById(shipmentIdReqVO.getShipmentId())
                && shipment.getStatus().equals("登记"))
            infoVO.setInfo("成功");
        else
            infoVO.setInfo("失败");
        return infoVO;
    }
}
