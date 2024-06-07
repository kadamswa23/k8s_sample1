package com.example.springjpa.security;

import com.example.springjpa.user.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.DefaultLoginPageConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    CustomUserDetailsService userDetailsService;
    @Autowired
    JwtSecurityFilter jwtSecurityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> {
                authorize.requestMatchers("/register/**","/swagger-ui/**","/swagger-ui/index.html").permitAll();
                authorize.requestMatchers("/auth/**","/api/v1.0/register").permitAll().anyRequest().permitAll();
            })
          .addFilterBefore(jwtSecurityFilter, UsernamePasswordAuthenticationFilter.class)

          .build();
    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer(){
        return (web) -> web.ignoring().requestMatchers("/register/**","/v3/api-docs","/swagger-ui/**","/swagger-ui/index.html");
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    UserDetailsService userDetailsService(){
        return userDetailsService;
    }

    @Bean
    AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider =  new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
