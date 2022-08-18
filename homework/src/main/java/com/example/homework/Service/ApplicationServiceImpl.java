package com.example.homework.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.example.homework.DAO.OrderHeaderMapper;
import com.example.homework.Domain.dto.OrderLineDTO;
import com.example.homework.Domain.dto.OrderListDTO;
import com.example.homework.Domain.entity.*;
import com.example.homework.Domain.service.*;
import com.example.homework.Domain.vo.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ApplicationServiceImpl implements ApplicationService {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private OrderHeaderMapper orderHeaderMapper;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerLocationService customerLocationService;
    @Autowired
    private OrderHeaderService orderHeaderService;
    @Autowired
    private OrderLineService orderLineService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ShipmentService shipmentService;

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
        InfoVO infoVO = new InfoVO();
        if (customerService.getById(customer.getCustomerId()).getStatus().equals("有效")) {
            customerService.saveOrUpdate(customer);

            // 处理地点信息
            Optional.ofNullable(customerInfoResVO.getLocations()).orElse(new ArrayList<>()).forEach(e -> {
                e.setCustomerId(customer.getCustomerId());
            });
            customerLocationService.saveOrUpdateBatch(customerInfoResVO.getLocations());
            infoVO.setInfo("保存成功");
        } else
            infoVO.setInfo("保存失败");
        return infoVO;
    }


    //    失效客户信息
    @Override
    public InfoVO disable(CustomerIdReqVO customerIdReqVO) {
        InfoVO infoVO = new InfoVO();
        QueryWrapper<OrderHeader> wrapper = new QueryWrapper<>();
        OrderHeader orderHeader = new OrderHeader();
        BeanUtils.copyProperties(customerIdReqVO, orderHeader);
        wrapper.eq("customer_id", orderHeader.getCustomerId());
        List<OrderHeader> list = orderHeaderService.list(wrapper);
        List<String> list1 = list.stream().map(OrderHeader::getStatus).collect(Collectors.toList());
        if ((customerService.getById(customerIdReqVO.getCustomerId()).getStatus().equals("有效")) && (list1.stream().allMatch(satus -> satus.equals("完成")))) {
            Customer customer = new Customer();
            BeanUtils.copyProperties(customerIdReqVO, customer);
            customerService.getById(customer.getCustomerId()).setStatus("失效");
            customerService.updateById(customer);
            infoVO.setInfo("更改成功");

        } else
            infoVO.setInfo("更改失败");
        return infoVO;
    }


    //    订单信息列表查询，支持分页查询
    @Override
    public OrderListResVO orderList(OrderListReqVO orderListReqVO) {
        Page<OrderListDTO> page = PageHelper.startPage(orderListReqVO.getPage(), orderListReqVO.getPageSize());
        List<OrderListDTO> list = orderHeaderMapper.orderList(orderListReqVO);
        OrderListResVO response = new OrderListResVO();
        if (!CollectionUtils.isEmpty(list)) {
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
        BeanUtils.copyProperties(orderDetailReqVO, orderLine);
        LambdaQueryWrapper<OrderLine> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderLine::getOrderId, orderLine.getOrderId());
        List<OrderLine> list = orderLineService.list(queryWrapper);
        List<OrderLineDTO> list1 = list.stream().map(line -> {
            OrderLineDTO orderDetailDTO = new OrderLineDTO();
            orderDetailDTO.setLineId(line.getLineId());
            orderDetailDTO.setItemId(line.getItemId());
            orderDetailDTO.setPrice(line.getPrice());
            orderDetailDTO.setQuantity(line.getQuantity());
            return orderDetailDTO;
        }).collect(Collectors.toList());
        OrderDetailResVO response = new OrderDetailResVO();
        if (!CollectionUtils.isEmpty(list)) {
            response.setOrderId(orderHeaderService.getById(orderLine.getOrderId()).getOrderId());
            response.setOrderNumber(orderHeaderService.getById(orderLine.getOrderId()).getOrderNumber());
            response.setCustomerId(orderHeaderService.getById(orderLine.getOrderId()).getCustomerId());
            response.setCustomerName(customerService.getById(orderHeaderService.getById(orderLine.getOrderId()).getCustomerId()).getCustomerName());
            response.setLines(list1);
        }
        return response;
    }


    //    订单头行保存接口，订单头行一起保存

    /**
     * 检查订单信息是否合法，校验通过返回true，否则返回false
     *
     * @param orderSaveReqVO
     * @param infoVO
     * @return
     */

    private boolean checkOrderHeader(OrderSaveReqVO orderSaveReqVO, InfoVO infoVO) {
        Customer customer = customerService.getById(orderSaveReqVO.getCustomerId());
        OrderHeader orderHeader = orderHeaderService.getById(orderSaveReqVO.getOrderId());
        if (ObjectUtils.isEmpty(orderSaveReqVO.getOrderId()) && ObjectUtils.isEmpty(orderSaveReqVO.getOrderNumber())) {
            return true;
        } else if (ObjectUtils.isEmpty(orderSaveReqVO.getOrderId()) || ObjectUtils.isEmpty(orderSaveReqVO.getOrderNumber())) {
            infoVO.setInfo("订单id或订单编码不能为空，失败");
            return false;
        }
        // 二者都有值
        // 检验通过订单ID获取的订单编码是否一致
        if (!orderSaveReqVO.getOrderNumber().equals(orderHeader.getOrderNumber())) {
            infoVO.setInfo("订单号与订单编码不一致，失败");
            return false;
        }
        // 检验订单状态是否允许修改
        if (!orderHeader.getStatus().equals("登记")) {
            infoVO.setInfo("无效的订单状态，失败");
            return false;
        }
        // 检验订单头的客户信息是否合法
        if (!orderSaveReqVO.getCustomerId().equals(customer.getCustomerId())) {
            infoVO.setInfo("无效的客户信息，失败");
            return false;
        }
    }

    /**
     * @param lines
     * @param infoVO
     * @return
     */
    private boolean checkOrderLine(List<OrderLineDTO> lines, InfoVO infoVO) {
        // 检验商品信息是否合法
        List<Integer> list = new ArrayList<>();
        lines.forEach(line -> {
            list.add(line.getItemId());
        });
        Boolean b = new Boolean(true);
        for (Integer itemId : list) {
            if (itemId.equals(itemService.getById(itemId).getItemId())) {
                b = true;
            } else {
                b = false;
                infoVO.setInfo("无效的商品信息，失败");
                break;
            }
        }
        return b;
    }

    /**
     * @param orderSaveReqVO
     * @param infoVO
     * @throws RuntimeException
     */
    @Transactional
    public void saveFunc(OrderSaveReqVO orderSaveReqVO, InfoVO infoVO) throws RuntimeException {
        // 保存头信息
        OrderHeader orderHeader = new OrderHeader();
        BeanUtils.copyProperties(orderSaveReqVO, orderHeader);
        if (ObjectUtils.isEmpty(orderHeader.getOrderId())) {
            orderHeader.setOrderNumber(seqGenerator("SO"));
            orderHeaderService.save(orderHeader);
            infoVO.setInfo("新建成功");
        } else {
            orderHeaderService.updateById(orderHeader);
            infoVO.setInfo("更新成功");
        }

        // 保存行信息
        List<OrderLine> list = new ArrayList<>();
        Optional.ofNullable(orderSaveReqVO.getLines()).orElse(new ArrayList<>()).forEach(info -> {
            OrderLine orderLine = new OrderLine();
            BeanUtils.copyProperties(info, orderLine);
            orderLine.setOrderId(orderHeader.getOrderId());
            list.add(orderLine);
        });
        try {
            orderLineService.saveOrUpdateBatch(list);
        } catch (Exception exception) {
            infoVO.setInfo("数据异常，操作失败");
        }
    }

    public String seqGenerator(String key) {
        //加上时间戳 如果不需要
        String datetime = new SimpleDateFormat("yyyMMdd").format(new Date());
        //查询 key 是否存在， 不存在返回 1 ，存在的话则自增加1
        Long autoID = redisTemplate.opsForValue().increment(key + datetime, 1);
        //这里是 4 位id，如果位数不够可以自行修改 ，下面的意思是 得到上面 key 的 值，位数为 4 ，不够	的话在左边补 0 ，比如  110 会变成  0110
        String value = StringUtils.leftPad(String.valueOf(autoID), 5, "0");
        //然后把 时间戳和优化后的 ID 拼接
        String code = MessageFormat.format("{0}{1}", key + datetime, value);
        //设置三天过期
        redisTemplate.expire(key + datetime, 1, TimeUnit.DAYS);
        return code;
    }

    @Override
    public InfoVO orderSave(OrderSaveReqVO orderSaveReqVO) {
        InfoVO infoVO = new InfoVO();
        // 订单头校验不通过，直接返回错误消息
        if (!checkOrderHeader(orderSaveReqVO, infoVO)) {
            return infoVO;
        }
        // 订单行校验不通过，直接返回错误消息
        if (!checkOrderLine(orderSaveReqVO.getLines(), infoVO)) {
            return infoVO;
        }

        // 保存操作
        saveFunc(orderSaveReqVO, infoVO);
        return infoVO;
    }

    /**
     * 校验输入信息是否合法
     * @param shipmentSaveReqVO
     * @return
     */
    private boolean checkLines(ShipmentSaveReqVO shipmentSaveReqVO){
        OrderLine orderLine= orderLineService.getById(shipmentSaveReqVO.getLines().get(0).getLineId());
        OrderHeader orderHeader = orderHeaderService.getById(orderLine.getOrderId());
        BigDecimal qu = new BigDecimal("o");
        shipmentSaveReqVO.getLines().forEach(quantity->{
            qu.add(quantity.getQuantity());
        });
        if (ObjectUtils.isNotEmpty(shipmentSaveReqVO.getLines().get(0).getLineId())
                &&shipmentSaveReqVO.getLines().get(0).getLineId().equals(orderLine.getLineId())
                &&orderHeader.getStatus().equals("登记")
                &&qu.equals(orderLine.getQuantity())){
                return true;
        }
        return false;
    }
    @Override
    public InfoVO shipmentSave(ShipmentSaveReqVO shipmentSaveReqVO) {
        InfoVO infoVO = new InfoVO();
        if(!checkLines(shipmentSaveReqVO))
            infoVO.setInfo("错误的输入信息，操作失败");
        else {
            String date=shipmentSaveReqVO.getLines().get(0).getEstimatedShipmentDate();
            if (date.equals(null)){


            }
        }
        return null;
    }
}
