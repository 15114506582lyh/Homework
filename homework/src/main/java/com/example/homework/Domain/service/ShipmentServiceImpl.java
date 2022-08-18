package com.example.homework.Domain.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.homework.DAO.ShipmentMapper;
import com.example.homework.Domain.dto.ShipmentListDTO;
import com.example.homework.Domain.entity.OrderHeader;
import com.example.homework.Domain.entity.Shipment;
import com.example.homework.Domain.vo.InfoVO;
import com.example.homework.Domain.vo.OrderDetailReqVO;
import com.example.homework.Domain.vo.ShipmenListAndSubmitReqVO;
import com.example.homework.Domain.vo.ShipmentListResVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShipmentServiceImpl extends ServiceImpl<ShipmentMapper, Shipment> implements ShipmentService {
    @Autowired
    ShipmentService shipmentService;

    /**
     * 某个订单行下的发货行查询接口
     *
     * @param shipmenListAndSubmitReqVO
     * @return
     */
    @Override
    public ShipmentListResVO shipmentList(ShipmenListAndSubmitReqVO shipmenListAndSubmitReqVO) {
        LambdaQueryWrapper<Shipment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ObjectUtils.isNotEmpty(shipmenListAndSubmitReqVO.getLineId()), Shipment::getLineId, shipmenListAndSubmitReqVO.getLineId());
        List<Shipment> list = shipmentService.list(queryWrapper);
        List<ShipmentListDTO> list1 = new ArrayList<>();
        ShipmentListResVO shipmentListResVO = new ShipmentListResVO();
        Optional.ofNullable(list).orElse(new ArrayList<>()).forEach(line -> {
            ShipmentListDTO shipmentListDTO = new ShipmentListDTO();
            BeanUtils.copyProperties(line, shipmentListDTO);
            list1.add(shipmentListDTO);
        });
        if (!CollectionUtils.isEmpty(list1))
            shipmentListResVO.setLines(list1);
        return shipmentListResVO;
    }

    @Override
    public InfoVO orderSubmit(ShipmenListAndSubmitReqVO shipmenListAndSubmitReqVO) {
        InfoVO infoVO = new InfoVO();
        LambdaQueryWrapper<Shipment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ObjectUtils.isNotEmpty(shipmenListAndSubmitReqVO.getLineId()), Shipment::getLineId, shipmenListAndSubmitReqVO.getLineId());
        List<Shipment> list = shipmentService.list(queryWrapper);
        Optional.ofNullable(list).orElse(new ArrayList<>()).forEach(line -> {
            if (line.getStatus().equals("登记")) {
                line.setStatus("待发货");
                update(queryWrapper);
                infoVO.setInfo("成功");
            } else
                infoVO.setInfo("失败");
        });
        return infoVO;
    }
}
