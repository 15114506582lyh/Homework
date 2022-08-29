package com.example.homework.Domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.homework.Domain.entity.Item;
import com.example.homework.Domain.vo.*;

public interface ItemService extends IService<Item> {
    ItemListResVO itemList(ItemListReqVO itemFind) throws Exception;// 商品信息列表查询，支持分页查询
    ItemResVO itemDetail(ItemIdReqVO itemIdReqVO) throws Exception;// 查询单个商品信息
    InfoVO itemCreate(ItemCreateReqVO itemCreateReqVO) throws Exception ;// 新建单个商品信息
    InfoVO itemUpdate(Item item) throws Exception ;// 更新单个商品信息
    InfoVO itemDisable(ItemIdReqVO itemIdReqVO) throws Exception ;// 下架商品

}
