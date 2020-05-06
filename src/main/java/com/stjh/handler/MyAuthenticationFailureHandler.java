package com.stjh.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义授权失败跳转handler
 * @author wuge
 * @date 2020/5/6 22:49
 */
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    /**
     * 跳转url
     */
    private String redirectUrl;

    /**
     * 有参构造方法
     * @param redirectUrl
     */
    public MyAuthenticationFailureHandler(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        // 失败跳转
        response.sendRedirect(redirectUrl);
    }
}
