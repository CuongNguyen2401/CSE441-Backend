package com.cuongnguyen.cse441.service;

import com.cuongnguyen.cse441.dto.request.RoleRequest;
import com.cuongnguyen.cse441.dto.response.RoleResponse;

import java.util.List;

public interface IRoleService {
    RoleResponse createRole(RoleRequest request);

    List<RoleResponse> getAllRoles();

    void deleteRole(String roleId);
}
