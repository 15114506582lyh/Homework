package com.example.homework.Domain.vo;

import com.example.homework.Domain.entity.Item;
import lombok.Data;

import java.util.List;
//response
@Data
public class ItemListResVO extends PaginationResultSupportImpl{

    List<Item> rows;

}
