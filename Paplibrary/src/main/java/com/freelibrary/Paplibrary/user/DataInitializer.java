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
            Role role = new Role();
            role.setName(roleType.name());
            roleRepository.save(role);
        }
    }
}