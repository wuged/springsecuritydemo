package com.stjh.service;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务
 * @author wuge
 * @date 2020/4/28 22:50
 */
public interface UserService {

    /**
     * 判断当前用户能否访问当前url
     * @param request
     * @param authentication
     * @return
     */
    Boolean hasPermission(HttpServletRequest request, Authentication authentication);
}
