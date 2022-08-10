package com.example.homework.Controller;

import com.example.homework.Domain.entity.Item;
import com.example.homework.Domain.vo.ItemAddReqVO;
import com.example.homework.Domain.vo.ItemListReqVO;
import com.example.homework.Domain.vo.ItemListResVO;
import com.example.homework.Service.ItemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(produces = "application/json")
public class Controller {
    @Autowired
    private ItemServiceImpl itemService;

    @PostMapping("/item")
    public List<Item> SelectAll(@RequestBody ItemListReqVO itemFind) {
        return itemService.SelectAll();
    }

    @PostMapping("/item/list")
    public ItemListResVO Find(@RequestBody ItemListReqVO itemFind) {
        return itemService.FindItem(itemFind);
    }
    @GetMapping("/item/detail")
    public Item selectById(Integer itemId){
        return  itemService.selectById(itemId);
    }
    @PostMapping("/item/create")
    public String addItem(@RequestBody @Validated ItemAddReqVO item){
        return itemService.addItem(item);
    }
}
