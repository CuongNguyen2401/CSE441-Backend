package com.cuongnguyen.cse441.mapper;

import com.cuongnguyen.cse441.dto.request.PermissionRequest;
import com.cuongnguyen.cse441.dto.response.PermissionResponse;
import com.cuongnguyen.cse441.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
