package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.security.access.prepost.PreAuthorize;

import com.example.demo.repository.CatOrderRepository;

@Service
public class OrderAdminService {

    private CatOrderRepository catOrderRepository;

    // 路由级别的访问限制只能限制访问某个路由，而不能限制访问某个方法。
    // 因此，为了确保只有管理员才能访问删除所有订单的方法，需要进行方法级别的访问限制。

    // 方法级别的访问限制可以通过在方法上添加 @PreAuthorize 注解来实现。
    //
    // @PreAuthorize 注解的参数是一个 SpEL 表达式，用于指定访问限制条件，条件的结果如果为 true，则允许访问，否则禁止访问。
    // SpEL 表达式的语法与 JavaScript 相似，例如：
    //   1. #username 表示方法的参数 username
    //   2. principal.username 表示当前登录用户的用户名
    //   3. hasRole('USER') 表示当前登录用户是否具有 USER 角色（USER 权限）
    //   4. hasAuthority('USER') 表示当前登录用户是否具有 USER 权限
    //   5. permitAll 表示允许所有用户访问
    //   6. denyAll 表示禁止所有用户访问
    //   7. hasIpAddress('IP') 表示指定 IP 地址的用户才能访问
    //   8. hasAnyRole('USER', 'ADMIN') 表示当前登录用户是否具有 USER 或 ADMIN 角色
    //   9. hasAnyAuthority('USER', 'ADMIN') 表示当前登录用户是否具有 USER 或 ADMIN 权限
    //  10. hasRole('USER') and hasIpAddress('IP') 表示当前登录用户是否具有 USER 角色并且 IP 地址为指定 IP 地址
    //  11. hasRole('USER') or hasIpAddress('IP') 表示当前登录用户是否具有 USER 角色或 IP 地址为指定 IP 地址
    //  12. hasRole('USER') and !hasIpAddress('IP') 表示当前登录用户是否具有 USER 角色并且 IP 地址不为指定 IP 地址
    //  13. hasRole('USER') and T(java.util.Calendar).getInstance().get(T(java.util.Calendar).DAY_OF_WEEK) == T(java.util.Calendar).TUESDAY 表示当前登录用户是否具有 USER 角色并且今天是星期二
    //  14. hasRole('USER') and #username == principal.username 表示当前登录用户是否具有 USER 角色并且方法的参数 username 与当前登录用户的用户名相同
    //  15. hasRole('USER') and #username == authentication.name 表示当前登录用户是否具有 USER 角色并且方法的参数 username 与当前登录用户的用户名相同
    //  16. hasRole('USER') and #username == authentication.principal.username 表示当前登录用户是否具有 USER 角色并且方法的参数 username 与当前登录用户的用户名相同
    //  17. hasRole('USER') and #username == authentication.principal['username'] 表示当前登录用户是否具有 USER 角色并且方法的参数 username 与当前登录用户的用户名相同
    //  18. hasRole('USER') and #username == authentication.details.username 表示当前登录用户是否具有 USER 角色并且方法的参数 username 与当前登录用户的用户名相同
    //  19. hasRole('USER') and #username == authentication.details['username'] 表示当前登录用户是否具有 USER 角色并且方法的参数 username 与当前登录用户的用户名相同
    //  20. hasRole('USER') and #username == authentication.details['remoteAddress'] 表示当前登录用户是否具有 USER 角色并且方法的参数 username 与当前登录用户的 IP 地址相同
    //  21. hasRole('USER') and #username == authentication.details['sessionId'] 表示当前登录用户是否具有 USER 角色并且方法的参数 username 与当前登录用户的会话 ID 相同
    //
    // 如果 @PreAuthorize 指定的条件结果为 false，那么方法调用将被阻止，Spring Security 将会抛出 AccessDeniedException。
    // AccessDeniedException 是一个非检查型异常，所以我们不需要捕获它，除非想要在异常处理中添加一些自定义的行为。
    // 如果我们不捕获 AccessDeniedException，它将会往上传递，最终被 Spring Security 的过滤器捕获并进行相应的处理。
    // Spring Security 对 AccessDeniedException 的处理是：要么返回HTTP 403页面，要么在用户没有认证的情况下重定向到登录页面。
    // 
    // @PreAuthorize 适合于方法调用之前进行访问限制的场景。
    // 如果想要在方法调用之后进行访问限制，那么可以使用 @PostAuthorize 注解。
    // 为什么要在方法调用之后进行访问限制呢？因为在方法调用之后，我们可以获取到方法的返回值，然后根据返回值来决定是否允许访问。
    // 例如：@PostAuthorize("hasRole('ADMIN') || "returnObject.user.username == authentication.name")
    //       这里的 returnObject 就是方法的返回值，于是可以通过判断返回值的 user.username 属性是否与当前登录用户的用户名相同来决定是否允许访问。
    // 
    // 要使 @PreAuthorize 或 @PostAuthorize 等方法访问控制注解生效，需要启用全局的方法安全功能，
    // 也就是在 SecurityConfiguration 中注解 @EnableGlobalMethodSecurity(prePostEnabled = true)。

    // 这里使用了 hasRole('ADMIN') 表达式，表示只有具有 ADMIN 角色（ADMIN 权限）的用户才能访问该方法。
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAllOrder() {
        catOrderRepository.deleteAll();
    }

}
