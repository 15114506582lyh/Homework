package com.example.homework.Controller;

import com.example.homework.Domain.entity.Item;
import com.example.homework.Domain.vo.ItemFindReqVO;
import com.example.homework.Domain.vo.ItemFindResVO;
import com.example.homework.Service.ItemServiceImpl;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Controller {
    @Autowired
    private ItemServiceImpl itemService;

    @PostMapping("/item")
    public List<Item> SelectAll(@RequestBody ItemFindReqVO itemFind) {
//        PageHelper.startPage(itemFind.getPagenum(), itemFind.getPagesize());
        return itemService.SelectAll();
    }

    @PostMapping("/item/list")
    public ItemFindResVO Find(@RequestBody ItemFindReqVO itemFind) {
        return itemService.FindItem(itemFind);
    }

}
