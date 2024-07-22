package com.vanlang.webbanhang.repository;

import com.vanlang.webbanhang.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    Role findRoleById(Long id);
    List<Role> getAllRoles();
    Role getRoleById(Long id);
    Role updateRole(Role role);
}
