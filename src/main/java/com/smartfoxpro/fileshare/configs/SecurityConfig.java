package com.smartfoxpro.fileshare.configs;

import com.smartfoxpro.fileshare.CustomAuthenticationProvider;
import com.smartfoxpro.fileshare.repository.UserRepository;
import com.smartfoxpro.fileshare.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String PERMIT_PATH = "/register";

    @Autowired
    private UserServiceImpl service;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserRepository repository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests().antMatchers(PERMIT_PATH).permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();

        http.headers().frameOptions().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(service)
                .passwordEncoder(encoder())
                .and()
                .authenticationProvider(authProvider())
                .jdbcAuthentication()
                .dataSource(dataSource);
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }


    @Bean
    public DaoAuthenticationProvider authProvider() {
        final CustomAuthenticationProvider authProvider
                = new CustomAuthenticationProvider(repository, service);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }
}