package com.example.homework.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.homework.Domain.entity.Item;
import com.example.homework.Domain.vo.ItemAddReqVO;
import com.example.homework.Domain.vo.ItemDisableReqVO;
import com.example.homework.Domain.vo.ItemListReqVO;
import com.example.homework.Domain.vo.ItemListResVO;

import java.util.List;

public interface ItemService extends IService<Item> {
    ItemListResVO list(ItemListReqVO itemFind);
    Item selectById(Integer id);
    String add(ItemAddReqVO itemAddReqVO);
    String update(Item item);
    String disable(ItemDisableReqVO itemDisableReqVO);

}
