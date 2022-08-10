package com.example.homework.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.homework.Domain.entity.Item;
import com.example.homework.Domain.vo.ItemAddReqVO;
import com.example.homework.Domain.vo.ItemListReqVO;
import com.example.homework.Domain.vo.ItemListResVO;

import java.util.List;

public interface ItemService extends IService<Item> {

    List<Item> SelectAll();
    ItemListResVO FindItem(ItemListReqVO itemFind);
    Item selectById(Integer id);
    String addItem(ItemAddReqVO itemAddReqVO);
    String updateItem(Item item);

}
