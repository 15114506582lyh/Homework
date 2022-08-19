package com.example.homework.Domain.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
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


import java.util.List;

@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements ItemService {
    @Autowired
    private ItemService itemService;

    /**
     * 商品信息列表查询，支持分页查询
     *
     * @param itemFind
     * @return
     */
    @Override
    public ItemListResVO itemList(ItemListReqVO itemFind) {
        LambdaQueryWrapper<Item> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtil.isNotEmpty(itemFind.getItemName()), Item::getItemName, itemFind.getItemName());
        queryWrapper.eq(StringUtil.isNotEmpty(itemFind.getStatus()), Item::getStatus, itemFind.getStatus());
        Page<Item> page = PageHelper.startPage(itemFind.getPage(), itemFind.getPageSize());
        List<Item> list = itemService.list(queryWrapper);
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

    /**
     * 查询单个商品信息
     *
     * @param itemIdReqVO
     * @return
     */
    @Override
    public Item itemDetail(ItemIdReqVO itemIdReqVO) {
        Item item = itemService.getById(itemIdReqVO.getItemId());
        if(ObjectUtils.isNotEmpty(item)) {
            if (ObjectUtils.isNotEmpty(itemIdReqVO.getItemId()) && itemIdReqVO.getItemId().equals(item.getItemId()))
                return getById(itemIdReqVO.getItemId());
            else return null;
        }else
            return null;
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
        if (StringUtil.isNotEmpty(itemCreateReqVO.getItemName())) {
            Item item = new Item();
            BeanUtils.copyProperties(itemCreateReqVO, item);
            try {
                save(item);
                infoVO.setInfo("操作成功");
            } catch (Exception exception) {
                infoVO.setInfo("参数有误，操作失败");
            }
        } else infoVO.setInfo("商品名称不能为空");
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
        Item item1 = itemService.getById(item.getItemId());
        if (ObjectUtils.isEmpty(item.getItemId())) {
            infoVO.setInfo("商品id不能为空，操作失败");
        } else {
            if (ObjectUtils.isNotEmpty(item1)
                    && item1.getItemId().equals(item.getItemId())) {
                try {
                    updateById(item);
                    infoVO.setInfo("操作成功");
                } catch (Exception exception) {
                    infoVO.setInfo("参数有误，操作失败");
                }
            } else infoVO.setInfo("商品不存在，操作失败");
        }
        return infoVO;
    }
    /**
     * 下架商品
     * @param itemIdReqVO
     * @return
     */
    @Override
    public InfoVO itemDisable(ItemIdReqVO itemIdReqVO) {
        InfoVO infoVO = new InfoVO();
        Item item = itemService.getById(itemIdReqVO.getItemId());
        if (ObjectUtils.isEmpty(itemIdReqVO.getItemId())){
            infoVO.setInfo("商品id不能为空，操作失败");
        }else {
            if (ObjectUtils.isNotEmpty(item)
                    &&itemIdReqVO.getItemId().equals(item.getItemId())){
                if (item.getStatus().equals("有效")){
                    item.setStatus("已下架");
                    updateById(item);
                    infoVO.setInfo("操作成功");
                }else
                    infoVO.setInfo("该商品不允许下架，操作失败");
            }else
                infoVO.setInfo("找不到此商品，操作失败");
        }
        return infoVO;
    }
}
