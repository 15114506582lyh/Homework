package com.example.homework.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.homework.Domain.entity.Item;
import com.example.homework.Domain.vo.ItemFindReqVO;
import com.example.homework.Domain.vo.ItemFindResVO;

import java.util.List;

public interface ItemService extends IService<Item> {
    List<Item> SelectAll();
    ItemFindResVO FindItem(ItemFindReqVO itemFind);

}
