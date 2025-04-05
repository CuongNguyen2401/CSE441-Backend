package com.thanhtan.groceryshop.mapper;

import com.thanhtan.groceryshop.dto.response.BaseDTO;
import com.thanhtan.groceryshop.entity.BaseEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BaseMapper {
    BaseDTO baseEntityToBaseDTO(BaseEntity baseEntity);
}