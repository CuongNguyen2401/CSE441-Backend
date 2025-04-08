package com.cuongnguyen.cse441.repository;

import com.cuongnguyen.cse441.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
    boolean existsByName(String name);
}