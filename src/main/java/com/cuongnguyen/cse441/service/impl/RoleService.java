package com.cuongnguyen.cse441.service.impl;


import com.cuongnguyen.cse441.dto.request.RoleRequest;
import com.cuongnguyen.cse441.dto.response.RoleResponse;
import com.cuongnguyen.cse441.entity.Role;
import com.cuongnguyen.cse441.exception.AppException;
import com.cuongnguyen.cse441.exception.ErrorCode;
import com.cuongnguyen.cse441.mapper.RoleMapper;
import com.cuongnguyen.cse441.repository.PermissionRepository;
import com.cuongnguyen.cse441.repository.RoleRepository;
import com.cuongnguyen.cse441.service.IRoleService;
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
