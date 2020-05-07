package com.stjh.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 用户实现类
 * @author wuge
 * @date 2020/4/28 22:51
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1.查询数据库判断用户名是否存在，如果不存在抛出UsernameNotFoundException

        if (!username.equals("admin")) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        // 2.把数据库的密码放到user构造方法中去，此处加密是为了模拟数据库中加密后的密码
        String password = bCryptPasswordEncoder.encode("123456");

        bCryptPasswordEncoder.matches("123456", password);
        // commaSeparatedStringToAuthorityList("权限，多个逗号相隔")
        return new User(username, password,
                // 如果要加角色以ROLE_ 开头，如下ROLE_abc即有abc这个角色名
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin,normal,ROLE_abc,/main.html"));
    }
}
