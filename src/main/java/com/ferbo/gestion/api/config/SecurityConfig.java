package com.ferbo.gestion.api.config;

import com.ferbo.gestion.api.filter.JwtAuthenticationFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig 
{
    @Autowired
    private JwtAuthenticationFilter jwtAtuthenticationFilter;
    
    /*
    * TODO: Al cambiar de version de SpringBoot de 2.7.18 -> 3.5.13, quitar 
    * los 'new AntPathRequestMatcher()' y solo deja la ruta '/gestion/**'
    * dentro de los .requestMatchers() 
    */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception 
    {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))// JWT es "stateless", no necesitamos sesiones de servidor
            .authorizeRequests(auth -> auth
                .requestMatchers(new AntPathRequestMatcher("/movil/autenticacion/token")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/gestion/**")).authenticated()
                .requestMatchers(new AntPathRequestMatcher("/movil/**")).authenticated()
                .anyRequest().authenticated()
            )
            .httpBasic(basic -> basic.disable())
            .formLogin(form -> form.disable())
            .addFilterBefore(jwtAtuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
            
        return http.build();
    }
    
}
