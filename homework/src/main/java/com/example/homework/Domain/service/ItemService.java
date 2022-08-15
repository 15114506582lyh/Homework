package com.example.homework.Domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.homework.Domain.entity.Item;
import com.example.homework.Domain.vo.*;

public interface ItemService extends IService<Item> {
    ItemListResVO list(ItemListReqVO itemFind);// 商品信息列表查询，支持分页查询
    Item selectById(Integer id);// 查询单个商品信息
    InfoVO add(ItemAddReqVO itemAddReqVO);// 新建单个商品信息
    InfoVO update(Item item);// 更新单个商品信息
    InfoVO disable(ItemDisableReqVO itemDisableReqVO);// 下架商品

}
