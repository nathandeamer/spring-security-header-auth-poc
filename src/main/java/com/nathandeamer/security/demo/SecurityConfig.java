package com.nathandeamer.security.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    static String X_SCOPES_HEADER = "x-my-custom-scopes";
    static String X_PRINCIPAL_HEADER = "x-my-custom-principal"; // a unique identifier for the user.

    // Any urls NOT in the ignoring request matchers list above will be required to have the custom headers.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .addFilterBefore(new ScopesAuthenticationFilter(), BasicAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)  // Disable what we don't need.
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable)
                .sessionManagement(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        // Order is important here.

                        // No authorities required on these endpoints
                        .requestMatchers("/actuator**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/noauth").permitAll()

                        // Authories required on these endpoints
                        .requestMatchers(HttpMethod.GET,"/auth").hasAuthority("customer:read")

                        // Deny all urls left
                        .anyRequest().denyAll()

                )
        .build();
    }
}
