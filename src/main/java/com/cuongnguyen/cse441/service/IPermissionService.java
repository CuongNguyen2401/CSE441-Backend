package com.cuongnguyen.cse441.service;

import com.cuongnguyen.cse441.dto.request.PermissionRequest;
import com.cuongnguyen.cse441.dto.response.PermissionResponse;

import java.util.List;

public interface IPermissionService {
    PermissionResponse createPermission(PermissionRequest request);

    List<PermissionResponse> getAllPermission();

    void deletePermission(String id);
}
