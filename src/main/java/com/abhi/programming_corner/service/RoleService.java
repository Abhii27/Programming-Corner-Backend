package com.abhi.programming_corner.service;

import com.abhi.programming_corner.model.Role;
import com.abhi.programming_corner.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role createRole(String roleName) {
        return roleRepository.save(new Role(roleName));
    }
}
