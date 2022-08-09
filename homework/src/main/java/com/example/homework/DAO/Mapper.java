package com.example.homework.DAO;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.homework.Domain.entity.Item;
import org.springframework.stereotype.Repository;

@Repository
public interface Mapper extends BaseMapper<Item> {
}
