package com.thanhtan.groceryshop.service.impl;


import com.thanhtan.groceryshop.dto.request.RoleRequest;
import com.thanhtan.groceryshop.dto.response.RoleResponse;
import com.thanhtan.groceryshop.entity.Role;
import com.thanhtan.groceryshop.exception.AppException;
import com.thanhtan.groceryshop.exception.ErrorCode;
import com.thanhtan.groceryshop.mapper.RoleMapper;
import com.thanhtan.groceryshop.repository.PermissionRepository;
import com.thanhtan.groceryshop.repository.RoleRepository;
import com.thanhtan.groceryshop.service.IRoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService implements IRoleService {

    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    @Override
    public RoleResponse createRole(RoleRequest request) {
        Role role = roleMapper.toRole(request);

        if (roleRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.RESOURCE_EXISTED);
        }

        role.setPermissions(new HashSet<>(permissionRepository.findAllById(request.getPermissions())));
        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).collect(Collectors.toList());
    }

    @Override
    public void deleteRole(String roleId) {
        roleRepository.deleteById(roleId);
    }


}
