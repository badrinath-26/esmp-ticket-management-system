package com.company.esmp.config;

import com.company.esmp.entity.Role;
import com.company.esmp.entity.User;
import com.company.esmp.repository.RoleRepository;
import com.company.esmp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class AdminUserInitializer {

    @Bean
    CommandLineRunner createAdminUser(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            if (userRepository.findByUsername("admin").isPresent()) {
                return; // admin already exists
            }

            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new RuntimeException("ADMIN role not found"));

            User admin = User.builder()
                    .username("admin")
                    .email("admin@esmp.com")
                    .password(passwordEncoder.encode("Admin@123"))
                    .enabled(true)
                    .roles(Set.of(adminRole))
                    .build();

            userRepository.save(admin);
        };
    }
}
