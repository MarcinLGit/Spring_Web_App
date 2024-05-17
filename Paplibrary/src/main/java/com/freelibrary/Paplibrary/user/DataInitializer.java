package com.freelibrary.Paplibrary.user;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import  com.freelibrary.Paplibrary.user.RoleRepository;


@Component
public class DataInitializer {

    private final RoleRepository roleRepository;

    @Autowired
    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void initData() {
        for (RoleType roleType : RoleType.values()) {
            // Sprawdź, czy rola już istnieje
            Role existingRole = roleRepository.findByName(roleType.name());
            if (existingRole == null) {
                // Jeśli rola nie istnieje, dodaj ją
                Role role = new Role();
                role.setName(roleType.name());
                roleRepository.save(role);
            }
        }
    }
}
