package com.example.homework.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.homework.DAO.OrderHeaderMapper;
import com.example.homework.Domain.dto.CustomerDetailDTO;
import com.example.homework.Domain.dto.OrderHeaderDTO;
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
            response.setInfo("查找成功");
            //返回查询总条数
            response.setTotal(page.getTotal());
            //返回总页数
            response.setTotalPages(page.getPages());
            //返回查询结果
            response.setRows(list);
        } else {
            response.setInfo("找不到该客户的信息");
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
        Customer customer = customerService.getById(customerIdReqVO.getCustomerId());
        CustomerDetailResVO detail = new CustomerDetailResVO();
        if (ObjectUtils.isNotEmpty(customer)) {
            BeanUtils.copyProperties(customer, detail);
            LambdaQueryWrapper<CustomerLocation> queryWrapper = Wrappers.lambdaQuery(CustomerLocation.class)
                    .eq(CustomerLocation::getCustomerId, customerIdReqVO.getCustomerId());
            List<CustomerLocation> list = customerLocationService.list(queryWrapper);
            List<CustomerDetailDTO> customerDetailDTOList = new ArrayList<>();
            CustomerDetailDTO customerDetailDTO = new CustomerDetailDTO();
            for (CustomerLocation customerLocation : list) {
                BeanUtils.copyProperties(customerLocation, customerDetailDTO);
                customerDetailDTOList.add(customerDetailDTO);
            }
            if (!CollectionUtils.isEmpty(customerDetailDTOList)) {
                detail.setLocations(customerDetailDTOList);
            } else {
                detail.setStatus("该客户目前无地址");
            }
        } else {
            detail.setCustomerId(customerIdReqVO.getCustomerId());
            detail.setStatus("找不到该客户");
        }
        return detail;
    }

    /**
     * 保存客户信息，包含收货地点信息一起保存
     *
     * @param customerSaveReqVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public InfoVO save(CustomerSaveReqVO customerSaveReqVO) throws Exception {
        InfoVO infoVO = new InfoVO();
        /**
         * 处理头信息
         */
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerSaveReqVO, customer);
        customerService.saveOrUpdate(customer);
        /**
         * 处理行信息
         */
        try {
            Optional.ofNullable(customerSaveReqVO.getLocations()).orElse(new ArrayList<>()).forEach(customerLocation -> {
                customerLocation.setCustomerId(customer.getCustomerId());
                customerLocationService.saveOrUpdate(customerLocation);
            });
            infoVO.setInfo("操作成功");
        } catch (Exception exception) {
            throw new Exception("发生未知错误，操作失败");
        }
        return infoVO;
    }

    /**
     * 失效客户信息
     *
     * @param customerIdReqVO
     * @return
     */
    @Override
    public InfoVO disable(CustomerIdReqVO customerIdReqVO) {
        InfoVO infoVO = new InfoVO();
        Customer customer = customerService.getById(customerIdReqVO.getCustomerId());
        if (ObjectUtils.isEmpty(customer)) {
            infoVO.setInfo("该用户不存在");
            return infoVO;
        }
        if (!("有效").equals(customer.getStatus())) {
            infoVO.setInfo("用户" + customer.getCustomerId() + "的状态为：" + customer.getStatus() + "，不能失效");
            return infoVO;
        }
        LambdaQueryWrapper<OrderHeader> wrapper = Wrappers.lambdaQuery(OrderHeader.class).
                eq(ObjectUtils.isNotEmpty(customer.getCustomerId()), OrderHeader::getCustomerId, customer.getCustomerId());
        List<OrderHeader> orderHeaders = orderHeaderService.list(wrapper);
        if (ObjectUtils.isNotEmpty(orderHeaders)) {
            for (OrderHeader orderHeader : orderHeaders) {
                if (!("完成").equals(orderHeader.getStatus()) || !("取消").equals(orderHeader.getStatus())) {
                    infoVO.setInfo("订单未完成，用户" + customer.getCustomerId() + "不能失效");
                    return infoVO;
                }
            }
        }
        customer.setStatus("失效");
        return infoVO;
    }

    /**
     * 订单信息列表查询，支持分页查询
     *
     * @param orderListReqVO
     * @return
     */
    @Override
    public OrderListResVO orderList(OrderListReqVO orderListReqVO) {
        Page<OrderListDTO> page = PageHelper.startPage(orderListReqVO.getPage(), orderListReqVO.getPageSize());
        List<OrderListDTO> list = orderHeaderMapper.orderList(orderListReqVO);
        OrderListResVO response = new OrderListResVO();
        if (!CollectionUtils.isEmpty(list)) {
            response.setInfo("查找成功");
            response.setTotal(page.getTotal());
            response.setTotalPages(page.getPages());
            response.setRows(list);
        } else {
            response.setInfo("找不到符合条件的订单");
        }
        return response;
    }


    //

    /**
     * 查询单个订单信息，包含订单头、订单行
     *
     * @param orderIdReqVO
     * @return
     */
    @Override
    public OrderDetailResVO orderDetail(OrderIdReqVO orderIdReqVO) {
        OrderHeader orderHeader = orderHeaderService.getById(orderIdReqVO.getOrderId());
        OrderDetailResVO orderDetailResVO = new OrderDetailResVO();
        if (ObjectUtils.isNotEmpty(orderHeader)) {
            /**
             * 处理订单头信息
             */
            Customer customer = customerService.getById(orderHeader.getCustomerId());
            BeanUtils.copyProperties(orderHeader, orderDetailResVO);
            orderDetailResVO.setCustomerName(customer.getCustomerName());
            /**
             * 处理订单行信息
             */
            LambdaQueryWrapper wrapper = Wrappers.lambdaQuery(OrderLine.class).
                    eq(ObjectUtils.isNotEmpty(orderHeader.getOrderId()), OrderLine::getOrderId, orderHeader.getOrderId());
            List<OrderLine> list = orderLineService.list(wrapper);
            if (ObjectUtils.isNotEmpty(list)) {
                List<OrderLineDTO> orderLineDTOS = new ArrayList<>();
                for (OrderLine orderLine : list) {
                    OrderLineDTO orderLineDTO = new OrderLineDTO();
                    BeanUtils.copyProperties(orderLine, orderLineDTO);
                    orderLineDTOS.add(orderLineDTO);
                }
                orderDetailResVO.setLines(orderLineDTOS);
                orderDetailResVO.setInfo("查找成功");
            } else {
                orderDetailResVO.setInfo("该订单暂时没有订单行信息");
            }
        } else {
            orderDetailResVO.setInfo("暂时没有订单头信息");
        }
        return orderDetailResVO;
    }

    /**
     * 订单头行保存接口，订单头行一起保存
     * 头校验
     * @param orderSaveReqVO
     * @return
     */
    public boolean headerCheck(OrderSaveReqVO orderSaveReqVO, InfoVO infoVO) {
        if (ObjectUtils.isNotEmpty(orderSaveReqVO.getOrderId()) && ObjectUtils.isNotEmpty(orderSaveReqVO.getOrderNumber())) {
            OrderHeader orderHeader = orderHeaderService.getById(orderSaveReqVO.getOrderId());
            if (ObjectUtils.isEmpty(orderHeader)) {
                infoVO.setInfo("找不到该订单");
                return false;
            }
            if (!orderHeader.getOrderNumber().equals(orderSaveReqVO.getOrderNumber())) {
                infoVO.setInfo("订单id与订单编码不匹配");
                return false;
            }
            if (!("登记").equals(orderHeader.getStatus())) {
                infoVO.setInfo("该订单的状态为：" + orderHeader.getStatus() + "，不允许更改");
                return false;
            }
        }
        Customer customer = customerService.getById(orderSaveReqVO.getCustomerId());
        if (ObjectUtils.isEmpty(customer)) {
            infoVO.setInfo("无效的客户信息");
            return false;
        }
        return true;
    }

    /**
     * 订单头行保存接口，订单头行一起保存
     *行校验
     * @param orderSaveReqVO
     * @param infoVO
     * @return
     */
    public boolean lineCheck(OrderSaveReqVO orderSaveReqVO, InfoVO infoVO){
        if (ObjectUtils.isNotEmpty(orderSaveReqVO.getLines())){
            for (OrderLineDTO orderLineDTO : orderSaveReqVO.getLines()){
                Item item = itemService.getById(orderLineDTO.getItemId());
                if (ObjectUtils.isEmpty(item)){
                    infoVO.setInfo("无效的商品信息");
                    return false;
                }
            }
        }
        return true;
    }
    public String createOrderNumber(String key) {
        //加上时间戳 如果不需要
        String datetime = new SimpleDateFormat("yyyyMMdd").format(new Date());
        //查询 key 是否存在， 不存在返回 1 ，存在的话则自增加1
        Long autoID = redisTemplate.opsForValue().increment(key + datetime, 1);
        //这里是 5 位id，如果位数不够可以自行修改 ，下面的意思是 得到上面 key 的 值，位数为 5 ，不够	的话在左边补 0 ，比如  110 会变成  0110
        String value = StringUtils.leftPad(String.valueOf(autoID), 5, "0");
        //然后把 时间戳和优化后的 ID 拼接
        String code = MessageFormat.format("{0}{1}",  key + datetime,value);
        return code;
    }

    /**
     *订单头行保存接口，订单头行一起保存
     * @param orderSaveReqVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public InfoVO orderSave(OrderSaveReqVO orderSaveReqVO) throws Exception {
        InfoVO infoVO = new InfoVO();
        if(!headerCheck(orderSaveReqVO,infoVO)){
            return infoVO;
        }
        if (!lineCheck(orderSaveReqVO,infoVO)){
            return infoVO;
        }
        /**
         * 处理头信息
         */
        OrderHeader orderHeader = new OrderHeader();
        BeanUtils.copyProperties(orderSaveReqVO,orderHeader);
        if (StringUtil.isEmpty(orderHeader.getOrderNumber())){
            orderHeader.setOrderNumber(createOrderNumber("SO"));
        }
        orderHeaderService.saveOrUpdate(orderHeader);
        /**
         * 处理行信息
         */
        try {
            Optional.ofNullable(orderSaveReqVO.getLines()).orElse(new ArrayList<>()).forEach(orderLineDTO -> {
                OrderLine orderLine = new OrderLine();
                BeanUtils.copyProperties(orderLineDTO, orderLine);
                orderLine.setOrderId(orderHeader.getOrderId());
                orderLineService.saveOrUpdate(orderLine);
            });
            infoVO.setInfo("操作成功");
        }catch (Exception exception){
            throw new Exception("发生未知错误，操作失败");
        }
        return infoVO;
    }

    /**
     * 订单发货行保存接口
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
        if (!(qu.compareTo(orderLine.getQuantity()) == 0)) {
            System.out.println(qu);
            System.out.println(orderLine.getQuantity());
            infoVO.setInfo("商品数量有误，操作失败");
            return false;
        }
        return true;
    }

    @Override
    public InfoVO shipmentSave(ShipmentSaveReqVO shipmentSaveReqVO) {
        InfoVO infoVO = new InfoVO();
        if (!checkShipment(shipmentSaveReqVO, infoVO)) {
            return infoVO;
        }
        Shipment shipment = new Shipment();
        Optional.ofNullable(shipmentSaveReqVO.getLines()).orElse(new ArrayList<>()).forEach(line -> {
            BeanUtils.copyProperties(line, shipment);
            if (StringUtils.isEmpty(shipment.getEstimatedShipmentDate())) {
                Date date;
                SimpleDateFormat sdf = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss ");
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 14);
                date = calendar.getTime();
                shipment.setEstimatedShipmentDate(sdf.format(date));
            }
            shipmentService.saveOrUpdate(shipment);
        });
        infoVO.setInfo("操作成功");
        return infoVO;
    }

    /**
     * 订单发货行确认发货接口
     *
     * @param shipmentIdReqVO
     * @return
     */
    @Override
    public InfoVO shipmentConfirm(ShipmentIdReqVO shipmentIdReqVO) {
        InfoVO infoVO = new InfoVO();
        Shipment shipment = shipmentService.getById(shipmentIdReqVO.getShipmentId());
        if (ObjectUtils.isEmpty(shipment.getShipmentId())) {
            infoVO.setInfo("发货行不存在");
            return infoVO;
        }
        if (StringUtils.isNotEmpty(shipment.getActualShipmentDate())) {
            infoVO.setInfo("实际发货日期不为空");
            return infoVO;
        }
        if (!shipment.getStatus().equals("待发货") && !shipment.getStatus().equals("发货中")) {
            infoVO.setInfo("订单状态不符");
            return infoVO;
        }
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss ");
        shipment.setActualShipmentDate(sdf.format(date));
        shipment.setStatus("已发货");
        shipmentService.updateById(shipment);
        OrderLine orderLine = orderLineService.getById(shipment.getLineId());
        OrderHeader orderHeader = orderHeaderService.getById(orderLine.getOrderId());
        if (("待发货").equals(orderHeader.getStatus())) {
            orderHeader.setStatus("发货中");
            orderHeaderService.updateById(orderHeader);
        }
        infoVO.setInfo("成功");
        return infoVO;
    }

    /**
     * 订单行删除接口
     *
     * @param orderLineIdReqVO
     * @return
     */
    @Override
    public InfoVO lineDelete(OrderLineIdReqVO orderLineIdReqVO) {
        InfoVO infoVO = new InfoVO();
        OrderLine orderLine = orderLineService.getById(orderLineIdReqVO.getLineId());
        if (ObjectUtils.isEmpty(orderLine)) {
            infoVO.setInfo("找不到订单行id为" + orderLineIdReqVO.getLineId() + "的信息");
            return infoVO;
        }
        OrderHeader orderHeader = orderHeaderService.getById(orderLine.getOrderId());
        if (!("登记").equals(orderHeader.getStatus())) {
            infoVO.setInfo("订单行id为" + orderLine.getLineId() + "的订单头状态为：" + orderHeader.getStatus() + "，不允许删除");
            return infoVO;
        }
        LambdaQueryWrapper queryWrapper = Wrappers.lambdaQuery(Shipment.class).
                eq(ObjectUtils.isNotEmpty(orderLineIdReqVO.getLineId()), Shipment::getLineId, orderLineIdReqVO.getLineId());
        List<Shipment> shipments = shipmentService.list(queryWrapper);
        Optional.ofNullable(shipments).orElse(new ArrayList<>()).forEach(shipment -> {
            shipmentService.removeById(shipment.getShipmentId());
        });
        orderLineService.removeById(orderLine.getLineId());
        infoVO.setInfo("删除成功");
        return infoVO;
    }

    /**
     * 订单发货行删除接口
     *
     * @param shipmentIdReqVO
     * @return
     */
    @Override
    public InfoVO shipmentDelete(ShipmentIdReqVO shipmentIdReqVO) {
        InfoVO infoVO = new InfoVO();
        Shipment shipment = shipmentService.getById(shipmentIdReqVO.getShipmentId());
        if (ObjectUtils.isEmpty(shipment)) {
            infoVO.setInfo("找不到发货行为" + shipmentIdReqVO.getShipmentId() + "的相关信息，无法删除");
            return infoVO;
        }
        OrderLine orderLine = orderLineService.getById(shipment.getLineId());
        OrderHeader orderHeader = orderHeaderService.getById(orderLine.getOrderId());
        if (!("登记").equals(orderHeader.getStatus())) {
            infoVO.setInfo("发货行为" + shipment.getShipmentId() + "的订单状态为：" + orderHeader.getStatus() + "，不允许删除");
            return infoVO;
        }
        shipmentService.removeById(shipment.getShipmentId());
        infoVO.setInfo("成功");
        return infoVO;
    }

    /**
     * 订单关闭接口
     *
     * @param orderIdReqVO
     * @return
     */
    @Override
    public InfoVO orderClose(OrderIdReqVO orderIdReqVO) {
        InfoVO infoVO = new InfoVO();
        LambdaQueryWrapper<OrderLine> wrapper = Wrappers.lambdaQuery(OrderLine.class).
                eq(ObjectUtils.isNotEmpty(orderIdReqVO.getOrderId()), OrderLine::getOrderId, orderIdReqVO.getOrderId());
        List<OrderLine> orderLines = orderLineService.list(wrapper);
        Optional.ofNullable(orderLines).orElse(new ArrayList<>()).forEach(orderLine -> {
            LambdaQueryWrapper<Shipment> queryWrapper = Wrappers.lambdaQuery(Shipment.class).
                    eq(ObjectUtils.isNotEmpty(orderLine.getLineId()), Shipment::getLineId, orderLine.getLineId());
            List<Shipment> shipments = shipmentService.list(queryWrapper);
            Optional.ofNullable(shipments).orElse(new ArrayList<>()).forEach(shipment -> {
                if (!("已发货").equals(shipment.getStatus())) {
                    infoVO.setInfo("订单" + orderLine.getOrderId() + "有未确认发货的发货行，不能删除");
                    return;
                }
            });
        });
        OrderHeader orderHeader = orderHeaderService.getById(orderLines.get(0).getOrderId());
        orderHeader.setStatus("完成");
        orderHeaderService.updateById(orderHeader);
        infoVO.setInfo("订单已完成");
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
        if (ObjectUtils.isEmpty(orderHeader)) {
            infoVO.setInfo("找不到id为" + orderIdReqVO.getOrderId() + "的订单");
            return infoVO;
        }
        if (!("登记").equals(orderHeader.getStatus()) || !("待发货").equals(orderHeader.getStatus())) {
            infoVO.setInfo("id为" + orderHeader.getOrderId() + "的订单状态为：" + orderHeader.getStatus() + "，不允许取消");
            return infoVO;
        }
        LambdaQueryWrapper<OrderLine> wrapper = Wrappers.lambdaQuery(OrderLine.class).
                eq(ObjectUtils.isNotEmpty(orderHeader.getOrderId()), OrderLine::getOrderId, orderHeader.getOrderId());
        List<OrderLine> orderLines = orderLineService.list(wrapper);
        Optional.ofNullable(orderLines).orElse(new ArrayList<>()).forEach(orderLine -> {
            LambdaQueryWrapper<Shipment> queryWrapper = Wrappers.lambdaQuery(Shipment.class).
                    eq(ObjectUtils.isNotEmpty(orderLine.getLineId()), Shipment::getLineId, orderLine.getLineId());
            List<Shipment> shipments = shipmentService.list(queryWrapper);
            Optional.ofNullable(shipments).orElse(new ArrayList<>()).forEach(shipment -> {
                shipment.setStatus("已取消");
                shipmentService.updateById(shipment);
            });
        });
        orderHeader.setStatus("已取消");
        orderHeaderService.updateById(orderHeader);
        infoVO.setInfo("订单已取消");
        return infoVO;
    }
}
