package com.example.homework.DAO;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.homework.Domain.dto.OrderListDTO;
import com.example.homework.Domain.entity.OrderHeader;
import com.example.homework.Domain.vo.OrderListReqVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface OrderHeaderMapper extends BaseMapper<OrderHeader> {

    @Select("<script>" +
            "select customer_name,order_number,order_date,group_concat(item_name) as item_name,sum(orderline.price*orderline.quantity) as total_price,orderheader.status from orderheader\n" +
            "inner join orderline on orderheader.order_id=orderline.order_id\n" +
            "inner join item on orderline.item_id=item.item_id\n" +
            "inner join customer on orderheader.customer_id=customer.customer_id\n" +
            "<where>" +
            "customer_name like '%${orderListReqVO.customerName}%'\n" +
            "and order_number like '%${orderListReqVO.orderNumber}%' and order_date &gt;= '${orderListReqVO.minOrderDate}' and order_date &lt;= '${orderListReqVO.maxOrderDate}'\n" +
            "</where>" +
            "group by order_number" +
            "</script>"
    )
    List<OrderListDTO> orderList(@Param("orderListReqVO") OrderListReqVO orderListReqVO);
}
