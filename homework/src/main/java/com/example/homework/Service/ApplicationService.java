package com.example.homework.Service;

import com.example.homework.Domain.vo.*;

public interface ApplicationService {
    CustomerListResVO customerList(CustomerListReqVO customerListReqVO) throws Exception;// 客户信息列表查询，支持分页查询
    CustomerDetailResVO customerDetail(CustomerIdReqVO customerIdReqVO) throws Exception;// 查询单个客户信息
    InfoVO save(CustomerSaveReqVO customerSaveReqVO) throws Exception;// 保存客户信息，包含收货地点信息一起保存
    InfoVO disable(CustomerIdReqVO customerIdReqVO) throws Exception ;// 失效客户信息
    OrderListResVO orderList(OrderListReqVO orderListReqVO) throws Exception ; // 订单信息列表查询，支持分页查询
    OrderDetailResVO orderDetail(OrderIdReqVO orderIdReqVO) throws Exception ; // 查询单个订单信息，包含订单头、订单行
    InfoVO orderSave(OrderSaveReqVO orderSaveReqVO) throws Exception; // 订单头行保存接口，订单头行一起保存
    InfoVO shipmentSave(ShipmentSaveReqVO shipmentSaveReqVO); // 订单发货行保存接口
    InfoVO shipmentConfirm(ShipmentIdReqVO shipmentIdReqVO); // 订单发货行确认发货接口
    InfoVO lineDelete(OrderLineIdReqVO orderLineIdReqVO); // 订单行删除接口
    InfoVO shipmentDelete(ShipmentIdReqVO shipmentIdReqVO); // 订单发货行删除接口
    InfoVO orderClose(OrderIdReqVO orderIdReqVO); // 订单关闭接口
    InfoVO orderCancel(OrderIdReqVO orderIdReqVO) throws Exception; // 订单取消接口
}
