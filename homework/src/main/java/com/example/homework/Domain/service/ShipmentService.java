package com.example.homework.Domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.homework.Domain.entity.Shipment;
import com.example.homework.Domain.vo.*;

public interface ShipmentService extends IService<Shipment> {
    ShipmentListResVO shipmentList (ShipmentLineIdReqVO shipmentLineIdReqVO); // 某个订单行下的发货行查询接口

}
