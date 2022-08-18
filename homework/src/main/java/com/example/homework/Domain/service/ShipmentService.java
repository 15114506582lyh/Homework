package com.example.homework.Domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.homework.Domain.entity.Shipment;
import com.example.homework.Domain.vo.InfoVO;
import com.example.homework.Domain.vo.OrderDetailReqVO;
import com.example.homework.Domain.vo.ShipmenListAndSubmitReqVO;
import com.example.homework.Domain.vo.ShipmentListResVO;

public interface ShipmentService extends IService<Shipment> {
    ShipmentListResVO shipmentList (ShipmenListAndSubmitReqVO shipmenListAndSubmitReqVO); // 某个订单行下的发货行查询接口
    InfoVO orderSubmit(ShipmenListAndSubmitReqVO shipmenListAndSubmitReqVO); // 订单头行提交接口
}
