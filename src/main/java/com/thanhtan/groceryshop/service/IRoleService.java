package com.thanhtan.groceryshop.service;

import com.thanhtan.groceryshop.dto.request.RoleRequest;
import com.thanhtan.groceryshop.dto.response.RoleResponse;

import java.util.List;

public interface IRoleService {
    RoleResponse createRole(RoleRequest request);

    List<RoleResponse> getAllRoles();

    void deleteRole(String roleId);
}
