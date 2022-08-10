package com.example.homework.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.homework.DAO.ItemMapper;
import com.example.homework.Domain.entity.Item;
import com.example.homework.Domain.vo.ItemAddReqVO;
import com.example.homework.Domain.vo.ItemListReqVO;
import com.example.homework.Domain.vo.ItemListResVO;
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
    public List<Item> SelectAll() {
        return itemMapper.selectList(null);
    }

    @Override
    public ItemListResVO FindItem(ItemListReqVO itemFind) {
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
    public Item selectById(Integer id) {
        return getById(id);
    }

    @Override
    public String addItem(ItemAddReqVO item) {
        Item createItem = new Item();
        BeanUtils.copyProperties(item, createItem);
        save(createItem);
        return "商品添加成功";

    }
}
