package com.cuongnguyen.cse441.service.impl;

import com.cuongnguyen.cse441.dto.request.PermissionRequest;
import com.cuongnguyen.cse441.dto.response.PermissionResponse;
import com.cuongnguyen.cse441.entity.Permission;
import com.cuongnguyen.cse441.exception.AppException;
import com.cuongnguyen.cse441.exception.ErrorCode;
import com.cuongnguyen.cse441.mapper.PermissionMapper;
import com.cuongnguyen.cse441.repository.PermissionRepository;
import com.cuongnguyen.cse441.service.IPermissionService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService implements IPermissionService {

    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;


    @Override
    @Transactional
    public PermissionResponse createPermission(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);

        if (permissionRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.RESOURCE_EXISTED);
        }
        return permissionMapper.toPermissionResponse(permissionRepository.save(permission));
    }

    @Override
    public List<PermissionResponse> getAllPermission() {
        List<Permission> permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).collect(Collectors.toList());
    }

    @Override
    public void deletePermission(String id) {
        permissionRepository.deleteById(id);
    }
}
