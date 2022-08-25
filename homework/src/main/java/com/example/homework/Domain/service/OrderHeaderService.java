package com.example.homework.Domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.homework.Domain.entity.OrderHeader;
import com.example.homework.Domain.vo.InfoVO;
import com.example.homework.Domain.vo.OrderIdReqVO;

public interface OrderHeaderService extends IService<OrderHeader> {
    InfoVO orderSubmit(OrderIdReqVO orderIdReqVO); // 订单头行提交接口
}
