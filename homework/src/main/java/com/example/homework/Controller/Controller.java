package com.example.homework.Controller;

import com.example.homework.Domain.entity.Item;
import com.example.homework.Domain.vo.*;
import com.example.homework.Service.CustomerBaseServiceImpl;
import com.example.homework.Service.CustomerInfoService;
import com.example.homework.Service.ItemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(produces = "application/json")
public class Controller {
    @Autowired
    private ItemServiceImpl itemService;
    @Autowired
    private CustomerBaseServiceImpl customerService;
    @Autowired
    private CustomerInfoService customerInfoService;

    @PostMapping("/item/list")
    public ItemListResVO itemList(@RequestBody ItemListReqVO itemListReqVO) {
        return itemService.list(itemListReqVO);
    }
    @GetMapping("/item/detail")
    public Item itemSelectById(Integer itemId){
        return  itemService.selectById(itemId);
    }
    @PostMapping("/item/create")
    public String itemAdd(@RequestBody @Validated ItemAddReqVO itemAddReqVO){
        return itemService.add(itemAddReqVO);
    }
    @PostMapping("/item/update")
    public String itemUpdate(@RequestBody Item item){
        return itemService.update(item);
    }
    @PostMapping("/item/disable")
    public String itemDisable(@RequestBody ItemDisableReqVO itemDisableReqVO){
        return itemService.disable(itemDisableReqVO);
    }
    @PostMapping("/customer/list")
    public CustomerListResVO customerList(@RequestBody CustomerListReqVO customerListReqVO){
        return customerService.list(customerListReqVO);
    }
    @GetMapping("/customer/detail")
    public CustomerInfoResVO customerInfoList(Integer id){
        return customerInfoService.list(id);
    }
}
