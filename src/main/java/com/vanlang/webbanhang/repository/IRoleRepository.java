package com.vanlang.webbanhang.repository;

import com.vanlang.webbanhang.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {

    Role findRoleById(long value);

    Role findByName(String roleUser);
}
