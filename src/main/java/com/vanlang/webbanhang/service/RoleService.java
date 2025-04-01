package com.vanlang.webbanhang.service;

import com.vanlang.webbanhang.model.Role;
import com.vanlang.webbanhang.repository.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();
    Role getRoleById(Long id);
    Role updateRole(Role role);
}

