package com.example.homework.DAO;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.homework.Domain.entity.OrderLine;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
public interface OrderLineMapper extends BaseMapper<OrderLine> {
}
