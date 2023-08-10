package com.example.demo;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfiguration {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        List<UserDetails> users = new ArrayList<>();
        // 构造用户的两种方式：
        //   方法1. 使用 User 类的构造器
        users.add(new User(
            "user", 
            passwordEncoder.encode("password"), 
            Arrays.asList(new SimpleGrantedAuthority("USER"))
        ));
        //   方法2. 使用 User 类的 withUsername()、password()、roles() 方法
        users.add(User
            .withUsername("admin")
            .password(passwordEncoder.encode("password"))
            .roles("USER", "ADMIN")
            .build()
        );
        return new InMemoryUserDetailsManager(users);
    }

}
