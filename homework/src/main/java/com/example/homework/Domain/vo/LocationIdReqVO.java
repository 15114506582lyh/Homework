package com.example.homework.Domain.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LocationIdReqVO {
    @NotNull(message = "地点id不能为空")
    private Integer locationId;
}
