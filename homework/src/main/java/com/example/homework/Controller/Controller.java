package com.example.homework.Controller;

import com.example.homework.Domain.entity.Item;
import com.example.homework.Domain.vo.*;
import com.example.homework.Domain.service.CustomerServiceImpl;
import com.example.homework.Domain.service.ItemServiceImpl;
import com.example.homework.Service.CustomerApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(produces = "application/json")
public class Controller {
    @Autowired
    private ItemServiceImpl itemService;
    @Autowired
    private CustomerApplicationService customerApplicationService;

    @PostMapping("/item/list")
//    商品信息列表查询，支持分页查询
    public ItemListResVO itemList(@RequestBody ItemListReqVO itemListReqVO) {
        return itemService.list(itemListReqVO);
    }
    @GetMapping("/item/detail")
//    查询单个商品信息
    public Item itemSelectById(Integer itemId){
        return  itemService.selectById(itemId);
    }
    @PostMapping("/item/create")
//    新建单个商品信息
    public ItemAddStringVO itemAdd(@RequestBody @Validated ItemAddReqVO itemAddReqVO){
        return itemService.add(itemAddReqVO);
    }
    @PostMapping("/item/update")
//    更新单个商品信息
    public ItemUpdateStringVO itemUpdate(@RequestBody Item item){
        return itemService.update(item);
    }
    @PostMapping("/item/disable")
//    下架商品
    public ItemDisableStringVO itemDisable(@RequestBody ItemDisableReqVO itemDisableReqVO){
        return itemService.disable(itemDisableReqVO);
    }
    @PostMapping("/customer/list")
//    客户信息列表查询，支持分页查询
    public CustomerListResVO customerList(@RequestBody CustomerListReqVO customerListReqVO){
        return customerApplicationService.list(customerListReqVO);
    }
    @GetMapping("/customer/detail")
//    查询单个客户信息
    public CustomerInfoResVO customerInfoList(Integer customerId){
        return customerApplicationService.list(customerId);
    }
    @PostMapping("/customer/save")
    public CustomerSaveStringVO save(@RequestBody CustomerInfoResVO customerInfoResVO){
        return customerApplicationService.save(customerInfoResVO);
    }
}
