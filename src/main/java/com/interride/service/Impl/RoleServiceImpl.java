package com.interride.service.Impl;

import com.interride.model.entity.Role;
import com.interride.model.enums.ERole;
import com.interride.repository.RoleRepository;
import com.interride.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    @Override
    public List<String> createRoles(){
        List<String> response = new ArrayList<>();
        for(ERole roleName : ERole.values()) {
            if (!roleRepository.findByNombre(roleName).isPresent()) {
                Role role = new Role();
                role.setNombre(roleName);
                response.add(roleName.name());
                roleRepository.save(role);
            }
        }
        return response;
    }
}
