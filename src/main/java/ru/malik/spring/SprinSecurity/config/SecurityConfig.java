package ru.malik.spring.SprinSecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import ru.malik.spring.SprinSecurity.service.PersonDetailsService;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final PersonDetailsService personDetailsService;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }
    Chse
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login","/auth/registration", "/auth/register").permitAll() // Разрешаем всем
                        .anyRequest().permitAll() // Все остальные запросы требуют входа
                )
                .formLogin(form -> form
                        .loginPage("/auth/login") // Страница логина
                        .loginProcessingUrl("/process_login") // URL обработки логина
                        .defaultSuccessUrl("/hello", true) // После успешного входа
                        .failureUrl("/auth/login?error") // При ошибке входа
                        .permitAll()
                )
                .logout(logout -> logout
                .logoutUrl("/logout") // URL для выхода
                .logoutSuccessUrl("/auth/login?logout") // Перенаправление после выхода
                .invalidateHttpSession(true) // Инвалидация сессии
                .deleteCookies("JSESSIONID") // Удаление куки
                .permitAll()
        );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(personDetailsService) // Используем кастомный UserDetailsService
                .passwordEncoder(passwordEncoder()); // Используем BCryptPasswordEncoder

        return authenticationManagerBuilder.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Используем BCryptPasswordEncoder для безопасности
    }
}