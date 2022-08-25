package com.example.homework.Domain.service;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.homework.DAO.OrderHeaderMapper;
import com.example.homework.Domain.entity.OrderHeader;
import com.example.homework.Domain.vo.InfoVO;
import com.example.homework.Domain.vo.OrderIdReqVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderHeaderServiceImpl extends ServiceImpl<OrderHeaderMapper, OrderHeader> implements OrderHeaderService{
    @Autowired
    private OrderHeaderService orderHeaderService;

    /**
     * 订单头行提交接口
     * @param orderIdReqVO
     * @return
     */
    @Override
    public InfoVO orderSubmit(OrderIdReqVO orderIdReqVO) {
        InfoVO infoVO = new InfoVO();
        OrderHeader orderHeader = orderHeaderService.getById(orderIdReqVO.getOrderId());
        if (ObjectUtils.isEmpty(orderHeader)){
            infoVO.setInfo("找不到订单头为"+orderIdReqVO.getOrderId()+"的信息");
            return infoVO;
        }
        if (!("登记").equals(orderHeader.getStatus())){
            infoVO.setInfo("订单头id为"+orderHeader.getOrderId()+"的状态为："+orderHeader.getStatus()+"，不允许修改");
            return infoVO;
        }
        orderHeader.setStatus("待发货");
        orderHeaderService.updateById(orderHeader);
        infoVO.setInfo("提交成功");
        return infoVO;
    }
}
