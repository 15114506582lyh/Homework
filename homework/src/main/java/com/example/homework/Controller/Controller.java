package com.example.homework.Controller;

import com.example.homework.Domain.entity.Item;
import com.example.homework.Domain.service.CustomerLocationService;
import com.example.homework.Domain.service.OrderHeaderService;
import com.example.homework.Domain.service.ShipmentService;
import com.example.homework.Domain.vo.*;
import com.example.homework.Domain.service.ItemServiceImpl;
import com.example.homework.Service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    /**
     * 商品信息列表查询，支持分页查询
     * @param itemListReqVO
     * @return
     */
    @PostMapping("/item/list")
    public ItemListResVO list(@RequestBody ItemListReqVO itemListReqVO) {
        return itemService.itemList(itemListReqVO);
    }
    /**
     * 查询单个商品信息
     * @param itemIdReqVO
     * @return
     */
    @GetMapping("/item/detail")
    public Item detail(ItemIdReqVO itemIdReqVO) {
        return itemService.itemDetail(itemIdReqVO);
    }
    /**
     * 新建单个商品信息
     * @param itemCreateReqVO
     * @return
     */
    @PostMapping("/item/create")
    public InfoVO create(@RequestBody ItemCreateReqVO itemCreateReqVO) {
        return itemService.itemCreate(itemCreateReqVO);
    }
    /**
     * 更新单个商品信息
     * @param item
     * @return
     */
    @PostMapping("/item/update")
    public InfoVO update(@RequestBody Item item) {
        return itemService.itemUpdate(item);
    }
    /**
     * 下架商品
     * @param itemIdReqVO
     * @return
     */
    @PostMapping("/item/disable")
    public InfoVO disable(@RequestBody ItemIdReqVO itemIdReqVO) {
        return itemService.itemDisable(itemIdReqVO);
    }
    /**
     * 客户信息列表查询，支持分页查询
     * @param customerListReqVO
     * @return
     */
    @PostMapping("/customer/list")
    public CustomerListResVO list(@RequestBody CustomerListReqVO customerListReqVO) {
        return applicationService.customerList(customerListReqVO);
    }
    /**
     * 查询单个客户信息
     * @param customerIdReqVO
     * @return
     */
    @GetMapping("/customer/detail")
    public CustomerDetailResVO detail(CustomerIdReqVO customerIdReqVO) {
        return applicationService.customerDetail(customerIdReqVO);
    }
    /**
     * 保存客户信息，包含收货地点信息一起保存
     * @param customerSaveReqVO
     * @return
     */
    @PostMapping("/customer/save")
    public InfoVO save(@RequestBody CustomerSaveReqVO customerSaveReqVO) {
        return applicationService.save(customerSaveReqVO);
    }


    //    失效客户信息
    @PostMapping("/customer/disable")
    public InfoVO disable(@RequestBody CustomerIdReqVO customerIdReqVO) {
        return applicationService.disable(customerIdReqVO);
    }


    //    删除客户收货地点
    @DeleteMapping("/customer/location/delete")
    public InfoVO delete(@RequestBody LocationIdReqVO locationIdReqVO) {
        return customerLocationService.locationDelete(locationIdReqVO);
    }


    //    客户地点选择器，查询单个客户下面的收货地点
    @PostMapping("/customer/location/selector")
    public List<LocationSelectorResVO> select(@RequestBody CustomerIdReqVO customerIdReqVO) {
        return customerLocationService.select(customerIdReqVO);
    }


    //    订单信息列表查询，支持分页查询
    @PostMapping("/order/list")
    public OrderListResVO list(@RequestBody OrderListReqVO orderListReqVO) {
        return applicationService.orderList(orderListReqVO);
    }


    //    查询单个订单信息，包含订单头、订单行
    @GetMapping("/order/detail")
    public OrderDetailResVO detail(OrderIdReqVO orderIdReqVO) {
        return applicationService.orderDetail(orderIdReqVO);
    }

    /**
     * 订单头行保存接口，订单头行一起保存
     *
     * @param orderSaveReqVO
     * @return
     */
    @PostMapping("/order/save")
    public InfoVO orderSave(@RequestBody OrderSaveReqVO orderSaveReqVO) {
        return applicationService.orderSave(orderSaveReqVO);
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
     * 某个订单行下的发货行查询接口
     *
     * @param shipmentListReqVO
     * @return
     */
    @PostMapping("/order/shipment/list")
    public ShipmentListResVO shipmentList(@RequestBody ShipmentLineIdReqVO shipmentListReqVO) {
        return shipmentService.shipmentList(shipmentListReqVO);
    }

    /**
     * 订单头行提交接口
     *
     * @param shipmentLineIdReqVO
     * @return
     */
    @PostMapping("/order/submit")
    public InfoVO submit(@RequestBody ShipmentLineIdReqVO shipmentLineIdReqVO) {
        return shipmentService.orderSubmit(shipmentLineIdReqVO);
    }

    /**
     * 订单发货行确认发货接口
     *
     * @param shipmentIdReqVO
     * @return
     */
    @PostMapping("/order/shipment/confirm")
    public InfoVO confirm(@RequestBody ShipmentIdReqVO shipmentIdReqVO) {
        return shipmentService.shipmentConfirm(shipmentIdReqVO);
    }

    /**
     * 订单行删除接口
     *
     * @param shipmentLineIdReqVO
     * @return
     */
    @DeleteMapping("/order/line/delete")
    public InfoVO delete(@RequestBody ShipmentLineIdReqVO shipmentLineIdReqVO) {
        return applicationService.lineDelete(shipmentLineIdReqVO);
    }

    /**
     * 订单发货行删除接口
     *
     * @param shipmentIdReqVO
     * @return
     */
    @DeleteMapping("/order/shipment/delete")
    public InfoVO shipmentDelete(@RequestBody ShipmentIdReqVO shipmentIdReqVO) {
        return shipmentService.shipmentDelete(shipmentIdReqVO);
    }

    /**
     * 订单关闭接口
     * @param orderIdReqVO
     * @return
     */
    @PostMapping("/order/close")
    public InfoVO orderClose(@RequestBody OrderIdReqVO orderIdReqVO){
        return applicationService.orderClose(orderIdReqVO);
    }

    /**
     * 订单取消接口
     * @param orderIdReqVO
     * @return
     */
    @PostMapping("/order/cancel")
    public InfoVO orderCancel(@RequestBody OrderIdReqVO orderIdReqVO){
        return applicationService.orderCancel(orderIdReqVO);
    }
}
