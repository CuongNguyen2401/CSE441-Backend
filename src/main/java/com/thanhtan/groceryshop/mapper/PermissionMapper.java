package com.thanhtan.groceryshop.mapper;

import com.thanhtan.groceryshop.dto.request.PermissionRequest;
import com.thanhtan.groceryshop.dto.response.PermissionResponse;
import com.thanhtan.groceryshop.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
