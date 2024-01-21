package com.example.microservicofrontend;

import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.Security;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class ConfiguracaoSeguranca {

/*
    @Bean
    // Authentication
    public UserDetailsService userDetailsService(PasswordEncoder encoder){
        UserDetails admin = User.withUsername("Admin")
                .password(encoder.encode("Admin"))
                .roles("ADMIN")
                .build();
        UserDetails user = User.withUsername("Joao")
                .password(encoder.encode("123"))
                .roles("NORMAL")
                .build();
        return new InMemoryUserDetailsManager(admin,user);
    }
*/

    @Autowired
    private ServicoUtilizadorCustom servicoUtilizadorCustom;

    @Autowired
    @Lazy
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(servicoUtilizadorCustom).passwordEncoder(passwordEncoder);
    }



    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login", "/logout", "/", "/register").permitAll()
                        .requestMatchers("/menu").hasAnyRole("ADMIN", "NORMAL", "PREMIUM")
                        .requestMatchers("/menu/perfil").hasAnyRole("NORMAL", "PREMIUM", "ADMIN")
                        .requestMatchers("/menu/tarefas").hasAnyRole("NORMAL", "PREMIUM")
                        .requestMatchers("/menu/perfil/**").hasRole("PREMIUM")
                        .requestMatchers("/menu/upload/**").hasAnyRole("PREMIUM", "NORMAL")
                        .requestMatchers("/menu/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(new CustomAuthenticationSuccessHandler())
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(new CustomLogoutSuccessHandler())

                )
                .addFilterAfter(new CustomFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedPage("/acesso-negado")
                );

        return http.build();
    }



}
