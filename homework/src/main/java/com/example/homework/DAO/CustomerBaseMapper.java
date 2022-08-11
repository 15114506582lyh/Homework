package com.example.homework.DAO;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.homework.Domain.entity.Customer;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerBaseMapper extends BaseMapper<Customer> {
}
