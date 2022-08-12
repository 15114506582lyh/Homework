package com.example.homework.Domain.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.homework.DAO.ItemMapper;
import com.example.homework.Domain.entity.Item;
import com.example.homework.Domain.service.ItemService;
import com.example.homework.Domain.vo.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import java.util.List;

@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements ItemService {
    @Autowired
    private ItemMapper itemMapper;

    @Override
//    商品信息列表查询，支持分页查询
    public ItemListResVO list(ItemListReqVO itemFind) {
        LambdaQueryWrapper<Item> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtil.isNotEmpty(itemFind.getItemName()), Item::getItemName, itemFind.getItemName());
        queryWrapper.eq(StringUtil.isNotEmpty(itemFind.getStatus()), Item::getStatus, itemFind.getStatus());
        Page<Item> page = PageHelper.startPage(itemFind.getPage(), itemFind.getPageSize());
        List<Item> list = itemMapper.selectList(queryWrapper);
        // 处理分页返回结果
        ItemListResVO response = new ItemListResVO();
        if (!CollectionUtils.isEmpty(list)) {
            //返回查询总条数
            response.setTotal(page.getTotal());
            //返回总页数
            response.setTotalPages(page.getPages());
            //返回查询结果
            response.setRows(list);
        }
        return response;
    }

    @Override
//    查询单个商品信息
    public Item selectById(Integer id) {
        return getById(id);
    }

    @Override
//    新建单个商品信息
    public ItemAddStringVO add(ItemAddReqVO itemAddReqVO) {
        Item createItem = new Item();
        BeanUtils.copyProperties(itemAddReqVO, createItem);
        save(createItem);
        ItemAddStringVO itemAddStringVO =new ItemAddStringVO();
        itemAddStringVO.setInfo("商品添加成功");
        return itemAddStringVO;
    }

    @Override
    public ItemUpdateStringVO update(Item item) {
//        更新单个商品信息
        ItemUpdateStringVO itemUpdateStringVO = new ItemUpdateStringVO();
        if (!(getById(item.getItemId()) == null)) {
            updateById(item);
            itemUpdateStringVO.setInfo("修改商品成功");
        } else {
            itemUpdateStringVO.setInfo("找不到此商品，修改商品失败");
        }
        return itemUpdateStringVO;
    }

    @Override
//    下架商品
    public ItemDisableStringVO disable(ItemDisableReqVO itemDisableReqVO) {
        ItemDisableStringVO itemDisableStringVO = new ItemDisableStringVO();
        if (getById(itemDisableReqVO.getItemId()).getStatus().equals("有效")) {
            Item item = new Item();
            BeanUtils.copyProperties(itemDisableReqVO, item);
            updateById(item);
            itemDisableStringVO.setInfo("商品下架成功");
        } else {
            itemDisableStringVO.setInfo("该商品已经下架，下架失败");
            }
        return itemDisableStringVO;
    }
}
