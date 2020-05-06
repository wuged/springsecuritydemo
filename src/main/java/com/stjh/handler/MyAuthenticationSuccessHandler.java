package com.stjh.handler;

import com.stjh.service.impl.UserDetailsServiceImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义授权成功跳转handler
 * @author wuge
 * @date 2020/5/6 22:31
 */
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * 跳转url
     */
    private String redirectUrl;

    /**
     * 有参构造方法
     * @param redirectUrl
     */
    public MyAuthenticationSuccessHandler(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 获取用户信息
        User user = (User) authentication.getPrincipal();
        System.out.println("用户名：" + user.getUsername());
        System.out.println("密码：" + user.getPassword()); // 密码为null，为了安全
        System.out.println("权限：" + user.getAuthorities()); // UserDetailsServiceImpl设置的权限
        // 跳转
        response.sendRedirect(redirectUrl);
    }
}
