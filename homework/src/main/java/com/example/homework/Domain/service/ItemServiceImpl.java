package com.example.homework.Domain.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.homework.DAO.ItemMapper;
import com.example.homework.Domain.entity.Item;
import com.example.homework.Domain.vo.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import java.sql.Wrapper;
import java.util.List;

@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements ItemService {
    @Autowired
    private ItemService itemService;

    /**
     * 商品信息列表查询，支持分页查询
     * @param itemListReqVO
     * @return
     */
    @Override
    public ItemListResVO itemList(ItemListReqVO itemListReqVO) {
        LambdaQueryWrapper<Item> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper = queryWrapper.like(StringUtil.isNotEmpty(itemListReqVO.getItemName()), Item::getItemName, itemListReqVO.getItemName());
        queryWrapper = queryWrapper.eq(StringUtil.isNotEmpty(itemListReqVO.getStatus()), Item::getStatus, itemListReqVO.getStatus());
        Page<Item> page = PageHelper.startPage(itemListReqVO.getPage(), itemListReqVO.getPageSize());
        List<Item> list = itemService.list(queryWrapper);
        // 处理分页返回结果
        ItemListResVO response = new ItemListResVO();
        if (!CollectionUtils.isEmpty(list)) {
            response.setInfo("查找成功");
            //返回查询总条数
            response.setTotal(page.getTotal());
            //返回总页数
            response.setTotalPages(page.getPages());
            //返回查询结果
            response.setRows(list);
        }else {
            response.setInfo("找不到符合条件的商品");
        }
        return response;
    }

    /**
     * 查询单个商品信息
     *
     * @param itemIdReqVO
     * @return
     */
    @Override
    public Item itemDetail(ItemIdReqVO itemIdReqVO) {
        Item item = itemService.getById(itemIdReqVO.getItemId());
        if (ObjectUtils.isNotEmpty(item)) {
            return getById(itemIdReqVO.getItemId());
        }else {
            Item item1 = new Item();
            item1.setItemId(itemIdReqVO.getItemId());
            item1.setStatus("找不到此商品");
            return item1;
        }
    }

    /**
     * 新建单个商品信息
     *
     * @param itemCreateReqVO
     * @return
     */
    @Override
    public InfoVO itemCreate(ItemCreateReqVO itemCreateReqVO) {
        InfoVO infoVO = new InfoVO();
        Item item = new Item();
        BeanUtils.copyProperties(itemCreateReqVO, item);
        if (save(item)) {
            infoVO.setInfo("添加成功");
        }else {
            infoVO.setInfo("添加失败");
        }
        return infoVO;
    }

    /**
     * 更新单个商品信息
     *
     * @param item
     * @return
     */
    @Override
    public InfoVO itemUpdate(Item item) {
        InfoVO infoVO = new InfoVO();
        if (ObjectUtils.isNotEmpty(itemService.getById(item.getItemId()))) {
            updateById(item);
            infoVO.setInfo("更新成功");
        }else {
            infoVO.setInfo("商品不存在，更新失败");
        }
        return infoVO;
    }

    /**
     * 下架商品
     *
     * @param itemIdReqVO
     * @return
     */
    @Override
    public InfoVO itemDisable(ItemIdReqVO itemIdReqVO) {
        InfoVO infoVO = new InfoVO();
        Item item = itemService.getById(itemIdReqVO.getItemId());
        if (ObjectUtils.isNotEmpty(item)){
            if (("有效").equals(item.getStatus())){
                item.setStatus("已下架");
                updateById(item);
                infoVO.setInfo("操作成功");
            }else {
                infoVO.setInfo("该商品不允许下架，操作失败");
            }
        }else {
            infoVO.setInfo("找不到此商品，操作失败");
        }
        return infoVO;
    }
}
