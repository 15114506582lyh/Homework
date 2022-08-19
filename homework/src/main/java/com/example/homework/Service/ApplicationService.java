package com.example.homework.Service;

import com.example.homework.Domain.vo.*;

public interface ApplicationService {
    CustomerListResVO customerList(CustomerListReqVO customerListReqVO);// 客户信息列表查询，支持分页查询
    CustomerDetailResVO customerDetail(CustomerIdReqVO customerIdReqVO);// 查询单个客户信息
    InfoVO save(CustomerSaveReqVO customerSaveReqVO);// 保存客户信息，包含收货地点信息一起保存
    InfoVO disable(CustomerIdReqVO customerIdReqVO);// 失效客户信息
    OrderListResVO orderList(OrderListReqVO orderListReqVO); // 订单信息列表查询，支持分页查询
    OrderDetailResVO orderDetail(OrderIdReqVO orderIdReqVO); // 查询单个订单信息，包含订单头、订单行
    InfoVO orderSave(OrderSaveReqVO orderSaveReqVO); // 订单头行保存接口，订单头行一起保存
    InfoVO shipmentSave(ShipmentSaveReqVO shipmentSaveReqVO); // 订单发货行保存接口
    InfoVO lineDelete(ShipmentLineIdReqVO shipmentLineIdReqVO); // 订单行删除接口
    InfoVO orderClose(OrderIdReqVO orderIdReqVO); // 订单关闭接口
    InfoVO orderCancel(OrderIdReqVO orderIdReqVO); // 订单取消接口
}
