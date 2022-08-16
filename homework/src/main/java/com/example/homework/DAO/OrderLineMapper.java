package com.example.homework.DAO;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.homework.Domain.entity.OrderLine;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderLineMapper extends BaseMapper<OrderLine> {
}
