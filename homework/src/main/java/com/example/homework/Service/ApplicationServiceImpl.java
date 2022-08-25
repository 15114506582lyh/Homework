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
        // 判断客户是否是否存在
        Customer customer = customerService.getById(customerIdReqVO.getCustomerId());
        if (ObjectUtils.isEmpty(customer)) {
            infoVO.setInfo("找不到该顾客");
            return infoVO;
        }

        // 判断当前客户状态是否可以失效


        // 获取该客户所有的订单，按订单头状态分组，判断订单头的状态是否满足客户失效条件







            /**
             * 通过客户id查找订单头信息
             */
            LambdaQueryWrapper<OrderHeader> lambdaQueryWrapper = Wrappers.lambdaQuery(OrderHeader.class).
                    eq(ObjectUtils.isNotEmpty(customerIdReqVO.getCustomerId()), OrderHeader::getCustomerId, customerIdReqVO.getCustomerId());
            List<OrderHeader> orderHeaders = orderHeaderService.list(lambdaQueryWrapper);
            if (ObjectUtils.isNotEmpty(orderHeaders)) {
                /**
                 * 通过订单头id查找订单行信息，如果有多个订单头id，进行循环遍历查找
                 */
                for (OrderHeader orderHeader : orderHeaders) {
                    LambdaQueryWrapper<OrderLine> queryWrapper = Wrappers.lambdaQuery(OrderLine.class).
                            eq(ObjectUtils.isNotEmpty(orderHeader.getOrderId()), OrderLine::getOrderId, orderHeader.getOrderId());
                    List<OrderLine> orderLines = orderLineService.list(queryWrapper);
                    if (ObjectUtils.isNotEmpty(orderLines)) {
                        /**
                         * 通过订单行id查找发货行信息，如果有多个订单行id，进行循环遍历查找
                         */
                        for (OrderLine orderLine : orderLines) {
                            LambdaQueryWrapper<Shipment> wrapper = Wrappers.lambdaQuery(Shipment.class).
                                    eq(ObjectUtils.isNotEmpty(orderLine.getLineId()), Shipment::getLineId, orderLine.getLineId());
                            List<Shipment> shipments = shipmentService.list(wrapper);
                            if (ObjectUtils.isNotEmpty(shipments)) {
                                /**
                                 * 判断发货行状态，如果有多个发货行，进行循环遍历判断
                                 */
                                for (Shipment shipment : shipments) {
                                    if (!("完成").equals(shipment.getStatus())) {
                                        infoVO.setInfo("该客户有订单未完成，不能失效");
                                        return infoVO;
                                    }
                                }
                            }
                        }
                    }
                    /**
                     * 判断订单头状态，如果有多个订单头，进行循环遍历判断
                     */
                    if (!("完成").equals(orderHeader.getStatus())) {
                        infoVO.setInfo("该客户有订单未完成，不能失效!");
                        return infoVO;
                    }
                }
            }
            /**
             * 订单头、发货行判断完毕，判断客户状态
             */
            if (("有效").equals(customer.getStatus())) {
                customer.setStatus("失效");
                customerService.updateById(customer);
                infoVO.setInfo("该客户状态已变更为：失效");
            } else {
                infoVO.setInfo("该客户(" + customer.getCustomerId() + ")的状态为：" + customer.getStatus() + "，不允许失效");
            }

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
                for (OrderLine orderLine : list){
                    OrderLineDTO orderLineDTO = new OrderLineDTO();
                    BeanUtils.copyProperties(orderLine,orderLineDTO);
                    orderLineDTOS.add(orderLineDTO);
                }
                orderDetailResVO.setLines(orderLineDTOS);
                orderDetailResVO.setInfo("查找成功");
            }else {
                orderDetailResVO.setInfo("该订单暂时没有订单行信息");
            }
        }else {
            orderDetailResVO.setInfo("暂时没有订单头信息");
        }
        return orderDetailResVO;
    }
    /**
     * 订单头行保存接口，订单头行一起保存
     */
    /**
     * 方法
     */
    /**
     * 检查订单头信息是否合法，校验通过返回true，否则返回false
     * @param orderSaveReqVO
     * @param infoVO
     * @return
     */

    private boolean checkOrderHeader(OrderSaveReqVO orderSaveReqVO, InfoVO infoVO) {
        /**
         * 校验订单头和订单编码不为空（更新状态）
         */
        if(!ObjectUtils.isNotEmpty(orderSaveReqVO.getOrderHeaderDTO().getOrderId())
                &&!ObjectUtils.isNotEmpty(orderSaveReqVO.getOrderHeaderDTO().getOrderNumber())){
            OrderHeader orderHeader = orderHeaderService.getById(orderSaveReqVO.getOrderHeaderDTO().getOrderId());
            /**
             * 校验订单头是否存在
             */
            if(ObjectUtils.isNotEmpty(orderHeader)){
                /**
                 * 校验订单头和订单编码是否一致
                 */
                if (!orderHeader.getOrderNumber().equals(orderSaveReqVO.getOrderHeaderDTO().getOrderNumber())){
                    infoVO.setInfo("订单头id与订单编码不一致");
                    return false;
                }
            }else {
                infoVO.setInfo("找不到此订单头");
                return false;
            }
            /**
             * 校验订单状态
             */
            if (!("登记").equals(orderHeader.getStatus())){
                infoVO.setInfo("错误的订单状态");
                return false;
            }
        }
        Customer customer = customerService.getById(orderSaveReqVO.getOrderHeaderDTO().getCustomerId());
        if (ObjectUtils.isNotEmpty(customer)){
            infoVO.setInfo("找不到该客户");
            return false;
        }
        return true;
    }

    /**
     * 检查订单行信息是否合法，校验通过返回true，否则返回false
     * @param lines
     * @param infoVO
     * @return
     */
    private boolean checkOrderLine(List<OrderLineDTO> lines, InfoVO infoVO) {
        for(OrderLineDTO orderLineDTO : lines){
            Item item = itemService.getById(orderLineDTO.getItemId());
            if(ObjectUtils.isEmpty(item)){
                infoVO.setInfo("找不到此商品");
                return false;
            }
        }
        return true;
    }

    /**
     * 自动生成订单编码
     * @param key
     * @return
     */
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
    /**
     * 主方法
     * @param orderSaveReqVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public InfoVO orderSave(OrderSaveReqVO orderSaveReqVO) throws Exception {
        InfoVO infoVO = new InfoVO();
        /**
         * 订单头校验不通过，直接返回错误消息
         */
        if (!checkOrderHeader(orderSaveReqVO, infoVO)) {
            return infoVO;
        }
        /**
         * 订单行校验不通过，直接返回错误消息
         */
        if (!checkOrderLine(orderSaveReqVO.getLines(), infoVO)) {
            return infoVO;
        }
        /**
         * 处理订单头信息
         */
        if(ObjectUtils.isEmpty(orderSaveReqVO.getOrderHeaderDTO().getOrderDate())){
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            orderSaveReqVO.getOrderHeaderDTO().setOrderDate(sdf.format(date));
        }
        OrderHeader orderHeader = new OrderHeader();
        BeanUtils.copyProperties(orderSaveReqVO,orderHeader);
        orderHeaderService.saveOrUpdate(orderHeader);
        /**
         * 处理订单行信息
         */
        try {
            Optional.ofNullable(orderSaveReqVO.getLines()).orElse(new ArrayList<>()).forEach(orderLineDTO -> {
                OrderLine orderLine = new OrderLine();
                BeanUtils.copyProperties(orderLineDTO, orderLine);
                orderLineService.saveOrUpdate(orderLine);
            });
            infoVO.setInfo("成功");
        }catch (Exception exception){
            throw  new Exception("未知错误，操作失败");
        }
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
