package com.company.esmp.config;

import com.company.esmp.entity.Role;
import com.company.esmp.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner loadRoles(RoleRepository roleRepository) {
        return args -> {
            List<String> roles = List.of(
                    "ADMIN",
                    "MANAGER",
                    "AGENT",
                    "CUSTOMER"
            );

            for (String roleName : roles) {
                roleRepository.findByName(roleName)
                        .orElseGet(() -> roleRepository.save(
                                Role.builder().name(roleName).build()
                        ));
            }
        };
    }
}
