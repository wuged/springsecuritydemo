package com.stjh.service.impl;

import com.stjh.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * 用户服务实现类(自定义方法实现权限控制)
 * @Author: wuge
 * @Date: 2020/5/7
 */
@Service
public class UserServiceImpl implements UserService {

    @Override
    public Boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        // 获取用户的所有权限（角色、权限、能访问的url）
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        // 判断权限中是否包含RequestURI
        return authorities.contains(new SimpleGrantedAuthority(request.getRequestURI()));
    }
}
