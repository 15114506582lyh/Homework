package com.example.homework.Domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.homework.Domain.entity.Item;
import com.example.homework.Domain.vo.*;

public interface ItemService extends IService<Item> {
    ItemListResVO itemList(ItemListReqVO itemFind);// 商品信息列表查询，支持分页查询
    Item itemDetail(ItemIdReqVO itemIdReqVO);// 查询单个商品信息
    InfoVO itemCreate(ItemCreateReqVO itemCreateReqVO);// 新建单个商品信息
    InfoVO itemUpdate(Item item);// 更新单个商品信息
    InfoVO itemDisable(ItemIdReqVO itemIdReqVO);// 下架商品

}
