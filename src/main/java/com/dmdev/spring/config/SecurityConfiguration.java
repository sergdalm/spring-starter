package com.dmdev.spring.config;

import com.dmdev.spring.database.entity.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static com.dmdev.spring.database.entity.Role.ADMIN;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration  /*extends WebSecurityConfigurerAdapter*/ {

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable() // отключение csrf
//                .authorizeRequests().anyRequest().authenticated()
//                .and()
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/login")
//                        .deleteCookies("JSESSIONID"))
//                .formLogin(login -> login
//                        .loginPage("/login")
//                        .defaultSuccessUrl("/users") // куда идти если аутентификация прошла успешно
//                        .permitAll()); // к странице login все имеет доступ, даже те кто не прошло аутентификацию
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
//                .csrf().disable() // отключение csrf
                .authorizeHttpRequests(urlConfig -> urlConfig
                        .antMatchers("/login", "/users/registration", "v3/api-docs/**", "/swagger-ui/**").permitAll()
                        .antMatchers("/users/{\\d+}/delete").hasAuthority(ADMIN.getAuthority())
                        .antMatchers("/admin/**").hasAuthority(ADMIN.getAuthority())
//                        .antMatchers("/**").denyAll()
                        .anyRequest().authenticated() // разрешён доступ ко всем для аутентифицированных пользователей
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // По умолчанию это logout
                        .logoutSuccessUrl("/login") // страница, куда перейдет пользователь после успешного logout'а
                        .deleteCookies("JSESSIONID")) // удаление cookies и так происходит по умолчанию
//                .httpBasic(Customizer.withDefaults()); // с дефолтными настройкам
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/users")) // куда идти если аутентификация прошла успешно
                .oauth2Login(config -> config
                        .loginPage("/login")
                        .defaultSuccessUrl("/users"));
//                        .permitAll()); // к странице login все имеет доступ, даже те кто не прошло аутентификацию

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
