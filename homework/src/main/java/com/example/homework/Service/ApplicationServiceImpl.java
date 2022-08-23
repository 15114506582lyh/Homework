package com.example.homework.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.homework.DAO.OrderHeaderMapper;
import com.example.homework.Domain.dto.CustomerDetailDTO;
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
import java.util.*;
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

    /**
     * 客户信息列表查询，支持分页查询
     *
     * @param customerListReqVO
     * @return
     */
    @Override
    public CustomerListResVO customerList(CustomerListReqVO customerListReqVO) {
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

    /**
     * 查询单个客户信息
     *
     * @param customerIdReqVO
     * @return
     */
    @Override
    public CustomerDetailResVO customerDetail(CustomerIdReqVO customerIdReqVO) {
//        LambdaQueryWrapper<CustomerLocation> queryWrapper = new LambdaQueryWrapper<>();
//        Customer customer = customerService.getById(customerIdReqVO.getCustomerId());
//        List<CustomerDetailDTO> customerDetailDTOList = new ArrayList<>();
        CustomerDetailResVO response = new CustomerDetailResVO();
//        if (ObjectUtils.isNotEmpty(customer)) {
//            if (ObjectUtils.isNotEmpty(customerIdReqVO.getCustomerId())) {
//                if (ObjectUtils.isNotEmpty(customer.getCustomerId())) {
//                    Wrappers.lambdaQuery(CustomerLocation.class).eq()
//                    queryWrapper.eq(CustomerLocation::getCustomerId, customerIdReqVO.getCustomerId());
//                    List<CustomerLocation> list = customerLocationService.list(queryWrapper);
//                    Optional.ofNullable(list).orElse(new ArrayList<>()).forEach(customerLocation -> {
//                        CustomerDetailDTO customerDetailDTO = new CustomerDetailDTO();
//                        BeanUtils.copyProperties(customerLocation, customerDetailDTO);
//                        customerDetailDTOList.add(customerDetailDTO);
//                    });
//                    if (!CollectionUtils.isEmpty(list)) {
//                        BeanUtils.copyProperties(customer, response);
//                        response.setLocations(customerDetailDTOList);
//                    } else
//                        response = null;
//                } else
//                    response = null;
//            } else
//                response = null;
//        } else
//            response = null;
        return response;
    }
    //

    /**
     * 保存客户信息，包含收货地点信息一起保存
     *
     * @param customerSaveReqVO
     * @return
     */
    @Override
    public InfoVO save(CustomerSaveReqVO customerSaveReqVO) {
        InfoVO infoVO = new InfoVO();
        Customer customer = new Customer();
        // 处理头信息
        if (ObjectUtils.isNotEmpty(customerSaveReqVO.getCustomerId())) {
            //更新
            customer = customerService.getById(customerSaveReqVO.getCustomerId());
            if (ObjectUtils.isNotEmpty(customer)) {
                if (customer.getStatus().equals("有效")) {
                    BeanUtils.copyProperties(customerSaveReqVO, customer);
                    customerService.updateById(customer);
                    infoVO.setInfo("操作成功(更新)");
                } else
                    infoVO.setInfo(("不合法的客户状态,操作失败(更新)"));
            } else
                infoVO.setInfo(("找不到该客户,操作失败(更新)"));
        } else {
            //新增
            BeanUtils.copyProperties(customerSaveReqVO, customer);
            customerService.save(customer);

        }
        // 处理行信息
//        for (CustomerLocation customerLocation: customerSaveReqVO.getLocations()){
//            if (ObjectUtils.isNotEmpty(customerLocation.getLocationId())){
//                //更新
//                if(customerLocation.getLocationId().equals())
//            }
//        }

//        if (customerService.getById(customer.getCustomerId()).getStatus().equals("有效")) {
//            customerService.saveOrUpdate(customer);
//
//            // 处理地点信息
//            Optional.ofNullable(customerDetailResVO.getLocations()).orElse(new ArrayList<>()).forEach(e -> {
//                e.setCustomerId(customer.getCustomerId());
//            });
//            customerLocationService.saveOrUpdateBatch(customerDetailResVO.getLocations());
//            infoVO.setInfo("保存成功");
//        } else
//            infoVO.setInfo("保存失败");
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
    public OrderDetailResVO orderDetail(OrderIdReqVO orderIdReqVO) {
        OrderLine orderLine = new OrderLine();
        BeanUtils.copyProperties(orderIdReqVO, orderLine);
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
        } else if (ObjectUtils.isEmpty(orderSaveReqVO.getOrderId())
                || ObjectUtils.isEmpty(orderSaveReqVO.getOrderNumber())) {
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
        return true;
    }

    /**
     * @param lines
     * @param infoVO
     * @return
     */
    private boolean checkOrderLine(List<OrderLineDTO> lines, InfoVO infoVO) {
//        StringBuider err
        for (OrderLineDTO line : lines) {
            Integer itemId = line.getItemId();
//            if () {
//
//            }
        }
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
        String datetime = new SimpleDateFormat("yyyyMMdd").format(new Date());
        //查询 key 是否存在， 不存在返回 1 ，存在的话则自增加1
        Long autoID = redisTemplate.opsForValue().increment(key + datetime, 1);
        //这里是 5 位id，如果位数不够可以自行修改 ，下面的意思是 得到上面 key 的 值，位数为 5 ，不够	的话在左边补 0 ，比如  110 会变成  0110
        String value = StringUtils.leftPad(String.valueOf(autoID), 5, "0");
        //然后把 时间戳和优化后的 ID 拼接
        String code = MessageFormat.format("{0}{1}", key + datetime, value);
        //设置1天过期
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
     *
     * @param shipmentSaveReqVO
     * @return
     */
    private boolean checkShipment(ShipmentSaveReqVO shipmentSaveReqVO, InfoVO infoVO) {

        Shipment shipment = new Shipment();
        Optional.ofNullable(shipmentSaveReqVO.getLines()).orElse(new ArrayList<>()).forEach(line -> {
            BeanUtils.copyProperties(line, shipment);
        });
        OrderLine orderLine = orderLineService.getById(shipment.getLineId());
        OrderHeader orderHeader = orderHeaderService.getById(orderLine.getOrderId());
        BigDecimal qu = new BigDecimal("0");
        for (Shipment shipment1 : shipmentSaveReqVO.getLines()) {
            qu = qu.add(shipment1.getQuantity());
        }

//        shipmentSaveReqVO.getLines().forEach(quantity -> {
//            System.out.println(quantity.getQuantity());
//            qu = qu.add(quantity.getQuantity());
//        });

        // 判断订单行id是否为空
        if (ObjectUtils.isEmpty(shipment.getLineId())) {
            infoVO.setInfo("订单行id为空，操作失败");
            return false;
        }
        // 判断订单行id是否存在
        if (!(shipment.getLineId().equals(orderLine.getLineId()))) {
            infoVO.setInfo("找不到订单信息，操作失败");
            return false;
        }
        // 判断订单状态
        if (!(orderHeader.getStatus().equals("登记"))) {
            infoVO.setInfo("订单状态错误，操作失败");
            return false;
        }
        // 判断商品数量
        if (!qu.equals(orderLine.getQuantity())) {
            infoVO.setInfo("商品数量有误，操作失败");
            return false;
        }
        return true;
    }

    @Override
    public InfoVO shipmentSave(ShipmentSaveReqVO shipmentSaveReqVO) {
        Shipment shipment = new Shipment();
        Optional.ofNullable(shipmentSaveReqVO.getLines()).orElse(new ArrayList<>()).forEach(line -> {
            BeanUtils.copyProperties(line, shipment);
        });

        InfoVO infoVO = new InfoVO();
        if (!checkShipment(shipmentSaveReqVO, infoVO)) {
        } else {
            if (shipment.getEstimatedShipmentDate().equals(null)) {
                Date date;
                SimpleDateFormat sdf = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss ");
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 14);
                date = calendar.getTime();
                shipmentSaveReqVO.getLines().get(0).setEstimatedShipmentDate(sdf.format(date));
            }
            shipmentService.saveOrUpdate(shipment);
        }
        return infoVO;
    }

    /**
     * 订单行删除接口
     *
     * @param shipmentLineIdReqVO
     * @return
     */
    @Override
    public InfoVO lineDelete(ShipmentLineIdReqVO shipmentLineIdReqVO) {
        InfoVO infoVO = new InfoVO();
        LambdaQueryWrapper<Shipment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ObjectUtils.isNotEmpty(shipmentLineIdReqVO.getLineId()), Shipment::getLineId, shipmentLineIdReqVO.getLineId());
        List<Shipment> list = shipmentService.list(queryWrapper);
        List<Integer> list1 = new ArrayList<>();
        Optional.ofNullable(list).orElse(new ArrayList<>()).forEach(line -> {
            if (line.getStatus().equals("登记")) {
                list1.add(line.getShipmentId());
            } else
                infoVO.setInfo("失败");
        });
        if (shipmentService.removeByIds(list1)) {
            orderLineService.removeById(shipmentLineIdReqVO.getLineId());
            infoVO.setInfo("成功");
        } else
            infoVO.setInfo("shibai");
        return infoVO;
    }

    @Override
    public InfoVO orderClose(OrderIdReqVO orderIdReqVO) {
        InfoVO infoVO = new InfoVO();
        LambdaQueryWrapper<OrderLine> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ObjectUtils.isNotEmpty(orderIdReqVO.getOrderId()), OrderLine::getOrderId, orderIdReqVO.getOrderId());
        List<String> stringList = new ArrayList<>();
        List<OrderLine> list = orderLineService.list(queryWrapper);
        Optional.ofNullable(list).orElse(new ArrayList<>()).forEach(line -> {
            Shipment shipment = new Shipment();
            shipment.setLineId(line.getLineId());
            LambdaQueryWrapper<Shipment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(ObjectUtils.isNotEmpty(shipment.getLineId()), Shipment::getLineId, shipment.getLineId());
            List<Shipment> l = shipmentService.list(lambdaQueryWrapper);
            Optional.ofNullable(l).orElse(new ArrayList<>()).forEach(newline -> {
                stringList.add(newline.getStatus());
            });
        });
        for (String s : stringList) {
            if (!s.equals("已发货")) {
                infoVO.setInfo("失败");
                return infoVO;
            }
        }
        OrderHeader orderHeader = orderHeaderService.getById(orderIdReqVO.getOrderId());
        orderHeader.setStatus("完成");
        orderHeaderService.updateById(orderHeader);
        infoVO.setInfo("成功");
        return infoVO;
    }

    /**
     * 订单取消接口
     *
     * @param orderIdReqVO
     * @return
     */
    @Override
    public InfoVO orderCancel(OrderIdReqVO orderIdReqVO) {
        InfoVO infoVO = new InfoVO();
        OrderHeader orderHeader = orderHeaderService.getById(orderIdReqVO.getOrderId());
        System.out.println(orderHeader.getStatus());
        if (!orderHeader.getStatus().equals("登记") && !orderHeader.getStatus().equals("待发货")) {
            System.out.println(orderHeader.getStatus());
            infoVO.setInfo("失败");
            return infoVO;
        } else {
            orderHeader.setStatus("取消");
            orderHeaderService.updateById(orderHeader);
            LambdaQueryWrapper<OrderLine> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ObjectUtils.isNotEmpty(orderIdReqVO.getOrderId()), OrderLine::getOrderId, orderIdReqVO.getOrderId());
            List<OrderLine> list = orderLineService.list(queryWrapper);
            Optional.ofNullable(list).orElse(new ArrayList<>()).forEach(line -> {
                Shipment shipment = new Shipment();
                shipment.setLineId(line.getLineId());
                LambdaQueryWrapper<Shipment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(ObjectUtils.isNotEmpty(shipment.getLineId()), Shipment::getLineId, shipment.getLineId());
                List<Shipment> l = shipmentService.list(lambdaQueryWrapper);
                Optional.ofNullable(l).orElse(new ArrayList<>()).forEach(newline -> {
                    newline.setStatus("取消");
                    shipmentService.updateById(newline);
                });
            });
            infoVO.setInfo("成功");
        }
        return infoVO;
    }
}
