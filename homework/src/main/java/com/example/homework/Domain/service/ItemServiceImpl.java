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
import java.util.ArrayList;
import java.util.List;

@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements ItemService {
    @Autowired
    private ItemService itemService;

    /**
     * 商品信息列表查询，支持分页查询
     *
     * @param itemListReqVO
     * @return
     */
    @Override
    public ItemListResVO itemList(ItemListReqVO itemListReqVO) throws Exception {
        LambdaQueryWrapper<Item> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper = queryWrapper.like(StringUtil.isNotEmpty(itemListReqVO.getItemName()), Item::getItemName, itemListReqVO.getItemName());
        queryWrapper = queryWrapper.eq(StringUtil.isNotEmpty(itemListReqVO.getStatus()), Item::getStatus, itemListReqVO.getStatus());
        Page<Item> page = PageHelper.startPage(itemListReqVO.getPage(), itemListReqVO.getPageSize());
        // 处理分页返回结果
        ItemListResVO response = new ItemListResVO();
        try {
            List<Item> list = itemService.list(queryWrapper);
            //返回查询总条数
            response.setTotal(page.getTotal());
            //返回总页数
            response.setTotalPages(page.getPages());
            //返回查询结果
            response.setRows(list);
        } catch (Exception exception) {
            throw new Exception("找不到符合条件的商品");
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
    public ItemResVO itemDetail(ItemIdReqVO itemIdReqVO) throws Exception {
        ItemResVO itemResVO = new ItemResVO();
        try {
            Item item = itemService.getById(itemIdReqVO.getItemId());
            BeanUtils.copyProperties(item, itemResVO);
            return itemResVO;
        } catch (Exception exception) {
            throw new Exception("找不到商品id为" + itemIdReqVO.getItemId() + "的商品");
        }
    }

    /**
     * 新建单个商品信息
     *
     * @param itemCreateReqVO
     * @return
     */
    @Override
    public InfoVO itemCreate(ItemCreateReqVO itemCreateReqVO) throws Exception {
        InfoVO infoVO = new InfoVO();
        Item item = new Item();
        BeanUtils.copyProperties(itemCreateReqVO, item);
        try {
            save(item);
            infoVO.setInfo("添加成功");
            return infoVO;
        } catch (Exception exception) {
            throw new Exception("添加失败");
        }
    }

    /**
     * 更新单个商品信息
     *
     * @param item
     * @return
     */
    @Override
    public InfoVO itemUpdate(Item item) throws Exception {
        InfoVO infoVO = new InfoVO();
        try {
            itemService.getById(item.getItemId());
            updateById(item);
            infoVO.setInfo("更新成功");
            return infoVO;
        } catch (Exception exception) {
            throw new Exception("商品不存在，更新失败");
        }
    }

    /**
     * 下架商品
     *
     * @param itemIdReqVO
     * @return
     */
    @Override
    public InfoVO itemDisable(ItemIdReqVO itemIdReqVO) throws Exception {
        InfoVO infoVO = new InfoVO();
        try {
            Item item = itemService.getById(itemIdReqVO.getItemId());
            if (!("有效").equals(item.getStatus())) {
                infoVO.setInfo("该商品不允许下架，操作失败");
                return infoVO;
            }
            item.setStatus("已下架");
            updateById(item);
            return infoVO;
        } catch (Exception exception) {
            throw new Exception("找不到商品id为" + itemIdReqVO.getItemId() + "的商品，操作失败");
        }

    }
}
