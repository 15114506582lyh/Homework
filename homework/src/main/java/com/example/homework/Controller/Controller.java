package com.example.homework.Controller;

import com.example.homework.Domain.entity.Item;
import com.example.homework.Domain.service.CustomerLocationService;
import com.example.homework.Domain.vo.*;
import com.example.homework.Domain.service.ItemServiceImpl;
import com.example.homework.Service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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



//    商品信息列表查询，支持分页查询
    @PostMapping("/item/list")
    public ItemListResVO itemList(@RequestBody ItemListReqVO itemListReqVO) {
        return itemService.list(itemListReqVO);
    }



//    查询单个商品信息
    @GetMapping("/item/detail")
    public Item itemSelectById(Integer itemId){
        return  itemService.selectById(itemId);
    }



//    新建单个商品信息
    @PostMapping("/item/create")
    public InfoVO itemAdd(@RequestBody @Validated ItemAddReqVO itemAddReqVO){
        return itemService.add(itemAddReqVO);
    }



//    更新单个商品信息
    @PostMapping("/item/update")
    public InfoVO itemUpdate(@RequestBody Item item){
        return itemService.update(item);
    }



//    下架商品
    @PostMapping("/item/disable")
    public InfoVO itemDisable(@RequestBody ItemDisableReqVO itemDisableReqVO){
        return itemService.disable(itemDisableReqVO);
    }



//    客户信息列表查询，支持分页查询
    @PostMapping("/customer/list")
    public CustomerListResVO customerList(@RequestBody CustomerListReqVO customerListReqVO){
        return applicationService.list(customerListReqVO);
    }



//    查询单个客户信息
    @GetMapping("/customer/detail")
    public CustomerInfoResVO customerInfoList(Integer customerId){
        return applicationService.list(customerId);
    }



//    保存客户信息，包含收货地点信息一起保存
    @PostMapping("/customer/save")
    public InfoVO save(@RequestBody CustomerInfoResVO customerInfoResVO){
        return applicationService.save(customerInfoResVO);
    }



//    失效客户信息
    @PostMapping("/customer/disable")
    public InfoVO disable(@RequestBody CustomerIdReqVO customerIdReqVO){
        return applicationService.disable(customerIdReqVO);
    }


//    删除客户收货地点
    @DeleteMapping("/customer/location/delete")
    public InfoVO delete(@RequestBody LocationIdReqVO locationIdReqVO){
        return customerLocationService.locationDelete(locationIdReqVO);
    }



//    客户地点选择器，查询单个客户下面的收货地点
    @PostMapping("/customer/location/selector")
    public List<LocationSelectorResVO> select(@RequestBody  CustomerIdReqVO customerIdReqVO){
        return customerLocationService.select(customerIdReqVO);
    }
}
