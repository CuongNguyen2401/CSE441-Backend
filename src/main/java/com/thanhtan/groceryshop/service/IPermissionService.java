package com.thanhtan.groceryshop.service;

import com.thanhtan.groceryshop.dto.request.PermissionRequest;
import com.thanhtan.groceryshop.dto.response.PermissionResponse;

import java.util.List;

public interface IPermissionService {
    PermissionResponse createPermission(PermissionRequest request);

    List<PermissionResponse> getAllPermission();

    void deletePermission(String id);
}
