package com.example.demo.domain;

import java.util.Arrays;
import java.util.Collection;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@Entity(name = "\"USER\"")  // 注意这里的表名要用双引号括起来，因为 USER 是 SQL 的关键字，否则所需的 USER 表将不会创建
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@RequiredArgsConstructor
public class User implements UserDetails {  // 用户（User）实现了 UserDetail 接口从而使其访问受限（如受认证、授权、启用状态、锁定状态、有效期等限制）

    private static final long serialVersionUID = 1L;  // 指定序列化版本号，这个版本号会被用来验证序列化对象的版本是否与本地版本相同。
                                                      // 如果不指定，那么每次编译时都会生成一个新的版本号，这样就会导致反序列化失败。
                                                      // 详见：https://zhuanlan.zhihu.com/p/347246506
                                                      // UserDetails 接口继承了 Serializable 接口，因此实现时需要指定 serialVersionUID。

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private final String username;
    private final String password;
    private final String fullname;
    private final String street;
    private final String city;
    private final String state;
    private final String zip;
    private final String phoneNumber;

    // 以下是 UserDetails 接口的方法实现

    @Override
    // 获取用户的角色（已授予的权限）列表
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 这里暂定所有用户都是 USER 角色（USER 权限）
        return Arrays.asList(new SimpleGrantedAuthority("USER"));
    }

    @Override
    // 判断用户账户是否未过期
    public boolean isAccountNonExpired() {
        // 这里暂定永不过期
        return true;
    }

    @Override
    // 判断用户账户是否被锁定
    public boolean isAccountNonLocked() {
        // 这里暂定永不锁定
        return true;
    }

    @Override
    // 判断用户凭证是否未过期
    public boolean isCredentialsNonExpired() {
        // 这里暂定永不过期
        return true;
    }

    @Override
    // 判断用户账号是否可用
    public boolean isEnabled() {
        // 这里暂定永远可用
        return true;
    }

}
