package com.abhi.programming_corner.service;

import com.abhi.programming_corner.model.Role;
import com.abhi.programming_corner.model.User;
import com.abhi.programming_corner.repository.RoleRepository;
import com.abhi.programming_corner.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public User loadUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User createUser(String email, String password) {
        String encodePassword = passwordEncoder.encode(password);
        return userRepository.save(new User(email, encodePassword));
    }

    public void assignRoleToUser(String email, String roleName) {
        User user = loadUserByEmail(email);
        Role role = roleRepository.findByName(roleName);
        user.assignRoleToUser(role);
    }
}
