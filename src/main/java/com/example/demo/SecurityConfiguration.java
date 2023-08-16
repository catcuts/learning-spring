    package com.example.demo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;

@Configuration
public class SecurityConfiguration {
    
    @Bean
    public PasswordEncoder passwordEncoder() {  // 实现这个方法，返回一个密码编码器实现，用于加密密码
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {  // 实现这个方法，返回一个用户信息查询函数，用于根据用户名查询用户信息
        return username -> {
            User user = userRepository.findByUsername(username);
            if (user != null) return user;
            else throw new UsernameNotFoundException("User '" + username + "' not found");
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {  // 实现这个方法，返回一个安全过滤器链，用于进行安全过滤
        // 注：HttpSecurity 支持配置的安全功能包括：
        //   1. 要求在为某个请求提供服务前满足特定的安全条件
        //   2. 配置自定义的登录页面
        //   3. 使用户能够退出应用
        //   4. 配置 CSRF 保护以预防 CSRF 攻击（Cross-Site Request Forgery，跨站请求伪造）
        return http
            .authorizeRequests()                               // 对于认证 HTTP 请求
                .antMatchers("/h2-console/**")                     //   若匹配这些请求
                    .permitAll()                                   //     则允许所有用户访问
                    .and()                                             //   然后
                        .csrf().ignoringAntMatchers("/h2-console/**")  //   忽略对于这些请求的 CSRF 保护
                    .and()                                             //   然后
                        .headers().frameOptions().sameOrigin()         //   允许同源的 iframe 访问
            .and()  // 然后
            // 另起一套配置，使上面的配置（例如允许同源 iframe 访问）只对于 /h2-console/** 请求生效
            .authorizeRequests()                // 对于认证 HTTP 请求时
                .antMatchers("/design", "/orders")  // 若匹配这些请求
                    .hasRole("USER")                //   则要求用户具有 USER 角色（USER 权限）
                .antMatchers("/", "/**")            // 若匹配这些请求
                    .permitAll()                    //   则允许所有用户访问
            .and()  // 然后
                .formLogin()                             // 对于表单登录（基于表单的认证方式）
                    .loginPage("/login")                 // 指定登录页面
                    .defaultSuccessUrl("/design", true)  // 指定登录成功后的默认跳转页面
                                                         //   第二个参数表示是否总是使用这个默认跳转页面，即使用户在登录前访问了其他页面
            .and()
                .oauth2Login()
                    .loginPage("/login")
            .and()  // 然后
                .logout()                                // 对于退出登录
                    .logoutSuccessUrl("/login")          // 指定退出登录成功后的默认跳转页面
            .and()  // and() 方法用于连接多个配置或最后的build()方法
            .build();
            // 注：
            //   1. 这里的配置是有顺序的，即先配置的规则优先级高于后配置的规则，因此这里的配置顺序是有意义的。
            //   2. hasRole()、permitAll() 等方法都是用于配置访问规则的，除此之外还有其他方法。
            //      例如：
            //      - hasAuthority() 方法用于配置访问规则，要求用户具有指定的权限
            //      - hasAnyAuthority() 方法用于配置访问规则，要求用户具有指定的任意权限
            //      - hasAnyRole() 方法用于配置访问规则，要求用户具有指定的任意角色
            //      - hasIpAddress() 方法用于配置访问规则，要求用户的 IP 地址与指定的 IP 地址匹配
            //      - anonymous() 方法用于配置访问规则，要求用户是匿名用户
            //      - authenticated() 方法用于配置访问规则，要求用户是已认证用户
            //      - rememberMe() 方法用于配置访问规则，要求用户是通过 remember-me 记住的用户
            //      - fullyAuthenticated() 方法用于配置访问规则，要求用户是通过 remember-me 记住的用户或已认证用户
            //      - denyAll() 方法用于配置访问规则，要求用户是拒绝访问的用户
            //      - access() 方法用于配置访问规则，要求用户满足指定的 sPEL（Spring 表达式语言）表达式
            //      - not() 方法用于配置访问规则，要求用户不满足指定的访问规则
            //   3. 也可以使用 access() 方法来配置访问规则，来达到上面配置同样的效果。
            //      例如：
            //      return http
            //         .authorizeHttpRequests()
            //             .antMatchers("/design", "/orders")
            //                 .access("hasRole('USER')")
            //             .antMatchers("/", "/**")
            //                 .access("permitAll()")
            //         .and()
            //         .build();
            //   4. access(sPEL（Spring 表达式语言）) 方法适用于需要更复杂、更灵活、非常规的访问规则的场景。
            //      例如：
            //      return http
            //          .authorizeHttpRequests()
            //              .antMatchers("/design", "/orders")
            //                  .access("hasRole('USER') && " +
            //                          "T(java.util.Calendar).getInstance().get(" +
            //                          "T(java.util.Calendar).DAY_OF_WEEK) == " +
            //                          "T(java.util.Calendar).TUESDAY")
            //              .antMatchers("/", "/**")
            //                  .access("permitAll()")
            //          .and()
            //          .build();
            //      这里的 sPEL（Spring 表达式语言）是一种表达式语言，用于在运行时计算值或对象图，例如：

    }

}
