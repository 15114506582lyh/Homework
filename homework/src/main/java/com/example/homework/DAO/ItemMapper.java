package com.example.homework.DAO;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.homework.Domain.entity.Item;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface ItemMapper extends BaseMapper<Item> {
}
