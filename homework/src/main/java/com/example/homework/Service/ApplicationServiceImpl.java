package com.example.homework.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.example.homework.DAO.OrderHeaderMapper;
import com.example.homework.Domain.dto.OrderDetailDTO;
import com.example.homework.Domain.dto.OrderListDTO;
import com.example.homework.Domain.entity.Customer;
import com.example.homework.Domain.entity.CustomerLocation;
import com.example.homework.Domain.entity.OrderHeader;
import com.example.homework.Domain.entity.OrderLine;
import com.example.homework.Domain.service.CustomerLocationService;
import com.example.homework.Domain.service.CustomerService;
import com.example.homework.Domain.service.OrderHeaderService;
import com.example.homework.Domain.service.OrderLineService;
import com.example.homework.Domain.vo.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApplicationServiceImpl implements ApplicationService {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerLocationService customerLocationService;
    @Autowired
    private OrderHeaderService orderHeaderService;
    @Autowired
    private OrderLineService orderLineService;
    @Autowired
    private OrderHeaderMapper orderHeaderMapper;



//    客户信息列表查询，支持分页查询
    @Override
    public CustomerListResVO list(CustomerListReqVO customerListReqVO) {
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtil.isNotEmpty(customerListReqVO.getCustomerNumber()), Customer::getCustomerNumber, customerListReqVO.getCustomerNumber());
        queryWrapper.like(StringUtil.isNotEmpty(customerListReqVO.getCustomerName()), Customer::getCustomerName, customerListReqVO.getCustomerName());
        queryWrapper.eq(StringUtil.isNotEmpty(customerListReqVO.getStatus()), Customer::getStatus, customerListReqVO.getStatus());
        Page<Customer> page = PageHelper.startPage(customerListReqVO.getPage(), customerListReqVO.getPageSize());
        List<Customer> list = customerService.list(queryWrapper);
        CustomerListResVO response = new CustomerListResVO();
        if (!CollectionUtils.isEmpty(list)) {
            //返回查询总条数
            response.setTotal(page.getTotal());
            //返回总页数
            response.setTotalPages(page.getPages());
            //返回查询结果
            response.setRows(list);
        }
        return response;
    }



//    查询单个客户信息
    @Override
    public CustomerInfoResVO list(Integer id) {
        LambdaQueryWrapper<CustomerLocation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), CustomerLocation::getCustomerId, id);
        List<CustomerLocation> list = customerLocationService.list(queryWrapper);
        CustomerInfoResVO response = new CustomerInfoResVO();
        Customer customer = customerService.getById(id);
        if (!CollectionUtils.isEmpty(list)) {
            response.setCustomerId(id);
            response.setCustomerNumber(customer.getCustomerNumber());
            response.setCustomerName(customer.getCustomerName());
            response.setCustomerType(customer.getCustomerType());
            response.setEmail(customer.getEmail());
            response.setStatus(customer.getStatus());
            response.setLocations(list);
        }
        return response;

    }



//    保存客户信息，包含收货地点信息一起保存
    @Override
    public InfoVO save(CustomerInfoResVO customerInfoResVO) {
        // 先处理头信息
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerInfoResVO, customer);
        InfoVO infoVO=new InfoVO();
        if(customerService.getById(customer.getCustomerId()).getStatus().equals("有效")) {
            customerService.saveOrUpdate(customer);

            // 处理地点信息
            Optional.ofNullable(customerInfoResVO.getLocations()).orElse(new ArrayList<>()).forEach(e -> {
                e.setCustomerId(customer.getCustomerId());
            });
            customerLocationService.saveOrUpdateBatch(customerInfoResVO.getLocations());
            infoVO.setInfo("保存成功");
        }else
            infoVO.setInfo("保存失败");
        return infoVO;
    }



//    失效客户信息
    @Override
    public InfoVO disable(CustomerIdReqVO customerIdReqVO) {
        InfoVO infoVO = new InfoVO();
        QueryWrapper<OrderHeader> wrapper = new QueryWrapper<>();
        OrderHeader orderHeader = new OrderHeader();
        BeanUtils.copyProperties(customerIdReqVO,orderHeader);
        wrapper.eq("customer_id",orderHeader.getCustomerId());
        List<OrderHeader> list=orderHeaderService.list(wrapper);
        List<String> list1 = list.stream().map(OrderHeader::getStatus).collect(Collectors.toList());
        if((customerService.getById(customerIdReqVO.getCustomerId()).getStatus().equals("有效"))&&(list1.stream().allMatch(satus->satus.equals("完成")))){
            Customer customer=new Customer();
            BeanUtils.copyProperties(customerIdReqVO,customer);
            customerService.getById(customer.getCustomerId()).setStatus("失效");
            customerService.updateById(customer);
            infoVO.setInfo("更改成功");

        }else
            infoVO.setInfo("更改失败");
        return infoVO;
    }



//    订单信息列表查询，支持分页查询
    @Override
    public OrderListResVO orderList(OrderListReqVO orderListReqVO) {
        Page<OrderListDTO> page = PageHelper.startPage(orderListReqVO.getPage(),orderListReqVO.getPageSize());
        List<OrderListDTO> list = orderHeaderMapper.orderList(orderListReqVO);
        OrderListResVO response = new OrderListResVO();
        if(!CollectionUtils.isEmpty(list)){
            response.setTotal(page.getTotal());
            response.setTotalPages(page.getPages());
            response.setRows(list);
        }
        return response;
    }



//    查询单个订单信息，包含订单头、订单行
    @Override
    public OrderDetailResVO orderDetail(OrderDetailReqVO orderDetailReqVO) {
        OrderLine orderLine = new OrderLine();
        BeanUtils.copyProperties(orderDetailReqVO,orderLine);
        LambdaQueryWrapper<OrderLine> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderLine::getOrderId, orderLine.getOrderId());
        List<OrderLine> list = orderLineService.list(queryWrapper);
        List<OrderDetailDTO> list1 = list.stream().map(line->{
            OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
            orderDetailDTO.setLineId(line.getLineId());
            orderDetailDTO.setItemId(line.getItemId());
            orderDetailDTO.setPrice(line.getPrice());
            orderDetailDTO.setQuantity(line.getQuantity());
            return orderDetailDTO;
        }).collect(Collectors.toList());
        OrderDetailResVO response = new OrderDetailResVO();
        if(!CollectionUtils.isEmpty(list)){
            response.setOrderId(orderHeaderService.getById(orderLine.getOrderId()).getOrderId());
            response.setOrderNumber(orderHeaderService.getById(orderLine.getOrderId()).getOrderNumber());
            response.setCustomerId(orderHeaderService.getById(orderLine.getOrderId()).getCustomerId());
            response.setCustomerName(customerService.getById(orderHeaderService.getById(orderLine.getOrderId()).getCustomerId()).getCustomerName());
            response.setLines(list1);
        }
        return response;
    }
}
