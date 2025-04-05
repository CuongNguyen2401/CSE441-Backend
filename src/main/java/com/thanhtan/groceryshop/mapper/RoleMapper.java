package com.thanhtan.groceryshop.mapper;

import com.thanhtan.groceryshop.dto.request.RoleRequest;
import com.thanhtan.groceryshop.dto.response.RoleResponse;
import com.thanhtan.groceryshop.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
