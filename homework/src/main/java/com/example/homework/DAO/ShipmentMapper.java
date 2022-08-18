package com.example.homework.DAO;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.homework.Domain.entity.Shipment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShipmentMapper extends BaseMapper<Shipment> {
}
