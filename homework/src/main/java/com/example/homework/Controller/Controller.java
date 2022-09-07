package com.example.homework.Controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.example.homework.Domain.entity.Item;
import com.example.homework.Domain.service.CustomerLocationService;
import com.example.homework.Domain.service.OrderHeaderService;
import com.example.homework.Domain.service.ShipmentService;
import com.example.homework.Domain.vo.*;
import com.example.homework.Domain.service.ItemServiceImpl;
import com.example.homework.Service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(produces = "application/json")
public class Controller {
    @Autowired
    private ItemServiceImpl itemService;
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private CustomerLocationService customerLocationService;
    @Autowired
    private ShipmentService shipmentService;
    @Autowired
    private OrderHeaderService orderHeaderService;

    @NacosValue(value = "${useLocalCache:false}", autoRefreshed = true)
    private boolean useLocalCache;

    @GetMapping(value = "/get")
    @ResponseBody
    public boolean get() {
        return useLocalCache;
    }

    /**
     * 商品信息列表查询，支持分页查询
     * @param itemListReqVO
     * @return
     */
    @PostMapping("/item/list")
    public ItemListResVO list(@RequestBody ItemListReqVO itemListReqVO) throws Exception {
        return itemService.itemList(itemListReqVO);
    }
    /**
     * 查询单个商品信息
     * @param itemIdReqVO
     * @return
     */
    @GetMapping("/item/detail")
    public ItemResVO detail(@Validated ItemIdReqVO itemIdReqVO) throws Exception {
        return itemService.itemDetail(itemIdReqVO);
    }
    /**
     * 新建单个商品信息
     * @param itemCreateReqVO
     * @return
     */
    @PostMapping("/item/create")
    public InfoVO create(@Validated @RequestBody ItemCreateReqVO itemCreateReqVO) throws Exception {
        return itemService.itemCreate(itemCreateReqVO);
    }
    /**
     * 更新单个商品信息
     * @param item
     * @return
     */
    @PostMapping("/item/update")
    public InfoVO update(@Validated @RequestBody Item item) throws Exception {
        return itemService.itemUpdate(item);
    }
    /**
     * 下架商品
     * @param itemIdReqVO
     * @return
     */
    @PostMapping("/item/disable")
    public InfoVO disable(@Validated @RequestBody ItemIdReqVO itemIdReqVO) throws Exception {
        return itemService.itemDisable(itemIdReqVO);
    }
    /**
     * 客户信息列表查询，支持分页查询
     * @param customerListReqVO
     * @return
     */
    @PostMapping("/customer/list")
    public CustomerListResVO list(@RequestBody CustomerListReqVO customerListReqVO) throws Exception {
        return applicationService.customerList(customerListReqVO);
    }
    /**
     * 查询单个客户信息
     * @param customerIdReqVO
     * @return
     */
    @GetMapping("/customer/detail")
    public CustomerDetailResVO detail(@Validated CustomerIdReqVO customerIdReqVO) throws Exception {
        return applicationService.customerDetail(customerIdReqVO);
    }
    /**
     * 保存客户信息，包含收货地点信息一起保存
     * @param customerSaveReqVO
     * @return
     */
    @PostMapping("/customer/save")
    public InfoVO save(@Validated @RequestBody CustomerSaveReqVO customerSaveReqVO) throws Exception {
        return applicationService.save(customerSaveReqVO);
    }
    /**
     * 失效客户信息
     * @param customerIdReqVO
     * @return
     */
    @PostMapping("/customer/disable")
    public InfoVO disable(@Validated @RequestBody CustomerIdReqVO customerIdReqVO) throws Exception {
        return applicationService.disable(customerIdReqVO);
    }

    /**
     * 删除客户收货地点
     * @param locationIdReqVO
     * @return
     */
    @DeleteMapping("/customer/location/delete")
    public InfoVO delete(@Validated @RequestBody LocationIdReqVO locationIdReqVO) throws Exception {
        return customerLocationService.locationDelete(locationIdReqVO);
    }
    /**
     * 客户地点选择器，查询单个客户下面的收货地点
     * @param customerIdReqVO
     * @return
     */
    @PostMapping("/customer/location/selector")
    public LocationSelectorResVO select(@Validated @RequestBody CustomerIdReqVO customerIdReqVO) throws Exception {
        return customerLocationService.select(customerIdReqVO);
    }
    /**
     * 订单信息列表查询，支持分页查询
     * @param orderListReqVO
     * @return
     */
    @PostMapping("/order/list")
    public OrderListResVO list(@RequestBody OrderListReqVO orderListReqVO) throws Exception {
        return applicationService.orderList(orderListReqVO);
    }

    /**
     * 查询单个订单信息，包含订单头、订单行
     * @param orderIdReqVO
     * @return
     */
    @GetMapping("/order/detail")
    public OrderDetailResVO detail(@Validated OrderIdReqVO orderIdReqVO) throws Exception {
        return applicationService.orderDetail(orderIdReqVO);
    }

    /**
     * 订单头行保存接口，订单头行一起保存
     *
     * @param orderSaveReqVO
     * @return
     */
    @PostMapping("/order/save")
    public InfoVO orderSave(@Validated @RequestBody OrderSaveReqVO orderSaveReqVO) throws Exception {
        return applicationService.orderSave(orderSaveReqVO);
    }
    /**
     * 某个订单行下的发货行查询接口
     *
     * @param shipmentListReqVO
     * @return
     */
    @PostMapping("/order/shipment/list")
    public ShipmentListResVO shipmentList(@Validated @RequestBody ShipmentLineIdReqVO shipmentListReqVO) {
        return shipmentService.shipmentList(shipmentListReqVO);
    }
    /**
     * 订单发货行保存接口
     *
     * @param shipmentSaveReqVO
     * @return
     */
    @PostMapping("/order/shipment/save")
    public InfoVO shipmentSave(@RequestBody ShipmentSaveReqVO shipmentSaveReqVO) {
        return applicationService.shipmentSave(shipmentSaveReqVO);
    }

    /**
     * 订单头行提交接口
     *
     * @param orderIdReqVO
     * @return
     */
    @PostMapping("/order/submit")
    public InfoVO submit(@Validated @RequestBody OrderIdReqVO orderIdReqVO) {
        return orderHeaderService.orderSubmit(orderIdReqVO);
    }

    /**
     * 订单发货行确认发货接口
     *
     * @param shipmentIdReqVO
     * @return
     */
    @PostMapping("/order/shipment/confirm")
    public InfoVO confirm(@Validated @RequestBody ShipmentIdReqVO shipmentIdReqVO) {
        return applicationService.shipmentConfirm(shipmentIdReqVO);
    }

    /**
     * 订单行删除接口
     *
     * @param orderLineIdReqVO
     * @return
     */
    @DeleteMapping("/order/line/delete")
    public InfoVO delete(@Validated @RequestBody OrderLineIdReqVO orderLineIdReqVO) {
        return applicationService.lineDelete(orderLineIdReqVO);
    }

    /**
     * 订单发货行删除接口
     *
     * @param shipmentIdReqVO
     * @return
     */
    @DeleteMapping("/order/shipment/delete")
    public InfoVO shipmentDelete(@Validated @RequestBody ShipmentIdReqVO shipmentIdReqVO) {
        return applicationService.shipmentDelete(shipmentIdReqVO);
    }

    /**
     * 订单关闭接口
     * @param orderIdReqVO
     * @return
     */
    @PostMapping("/order/close")
    public InfoVO orderClose(@Validated @RequestBody OrderIdReqVO orderIdReqVO){
        return applicationService.orderClose(orderIdReqVO);
    }

    /**
     * 订单取消接口
     * @param orderIdReqVO
     * @return
     */
    @PostMapping("/order/cancel")
    public InfoVO orderCancel(@Validated @RequestBody OrderIdReqVO orderIdReqVO) throws Exception {
        return applicationService.orderCancel(orderIdReqVO);
    }
}
