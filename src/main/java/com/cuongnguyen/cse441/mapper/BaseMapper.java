package com.cuongnguyen.cse441.mapper;

import com.cuongnguyen.cse441.dto.response.BaseDTO;
import com.cuongnguyen.cse441.entity.BaseEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BaseMapper {
    BaseDTO baseEntityToBaseDTO(BaseEntity baseEntity);
}