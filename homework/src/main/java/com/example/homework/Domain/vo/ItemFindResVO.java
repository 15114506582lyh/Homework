package com.example.homework.Domain.vo;

import com.example.homework.Domain.entity.Item;
import lombok.Data;

import java.util.List;

@Data
public class ItemFindResVO extends PaginationResultSupportImpl{

    List<Item> rows;

}
