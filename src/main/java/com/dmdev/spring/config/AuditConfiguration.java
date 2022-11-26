package com.dmdev.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@Configuration
// Можно было поставить эту аннотацию над ApplicationRunner,
// но мы создали отдельную конфигурацию для этого
@EnableJpaAuditing
public class AuditConfiguration {

    @Bean
    public AuditorAware<String> auditorAware() {
        // SecurityContext.getCurrentUser.getEmail()

        // Сохраняем в БД username пользователя, который произвёл действие
        return () -> Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                // По правилу хорошего тона используем UserDetails
                .map(authentication -> (UserDetails) authentication.getPrincipal())
                .map(UserDetails::getUsername);
    }
}
