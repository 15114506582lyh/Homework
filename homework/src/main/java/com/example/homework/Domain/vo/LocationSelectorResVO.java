package com.example.homework.Domain.vo;

import com.example.homework.Domain.dto.LocationSelectorDTO;
import lombok.Data;

import java.util.List;

@Data
public class LocationSelectorResVO {
    private List<LocationSelectorDTO> locations;
}
