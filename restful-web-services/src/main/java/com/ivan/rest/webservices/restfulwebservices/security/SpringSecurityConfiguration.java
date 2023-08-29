package com.ivan.rest.webservices.restfulwebservices.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //request authenticated
    http.authorizeHttpRequests(
        auth -> auth.anyRequest().authenticated()
);

        // Show page if not authenticated.

        http.httpBasic(Customizer.withDefaults());

        //allow post disabling csrf

        http.csrf(csrf -> csrf.disable());

        return http.build();
    }
}
