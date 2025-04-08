package com.cuongnguyen.cse441.mapper;

import com.cuongnguyen.cse441.dto.request.RoleRequest;
import com.cuongnguyen.cse441.dto.response.RoleResponse;
import com.cuongnguyen.cse441.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
