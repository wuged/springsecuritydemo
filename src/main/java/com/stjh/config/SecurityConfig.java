package com.stjh.config;

import com.stjh.handler.MyAuthenticationFailureHandler;
import com.stjh.handler.MyAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Security配置
 * @author wuge
 * @date 2020/4/28 22:47
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 定义加密算法并注入到spring容器
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 表单认证
        http.formLogin()                      // 表单控制
                //.usernameParameter("username123") // 用户名别名，和表单一致。不配置的话默认username
                //.passwordParameter("password123") // 密码别名，和表单一致。不配置的话默认password
                .loginPage("/login.html")     // 登录页面
                .loginProcessingUrl("/login") // 拦截的登录请求（form表单中的），告诉springsecurity走UserDetailsServiceImpl类验证
                .successForwardUrl("/toMain") // 成功后跳转的请求（必须是post请求），跳转到main.html必须靠controller跳转，且跳转只能跳转项目内的url（请求转发），
                                              // 如需跳转到百度需自定义
                //.successHandler(new MyAuthenticationSuccessHandler("http://www.baidu.com")) // 自定义成功跳转页面
                .failureForwardUrl("/toFail") // 失败（和成功类似）
                //.failureHandler(new MyAuthenticationFailureHandler("fail.html"))  // 自定义失败跳转页面，这样就不需要控制器跳转到html页面了，而且可以跳转到任何url
        ;
        // url 拦截
        http.authorizeRequests()
                .antMatchers("/login.html").permitAll() // 登录页不需要认证
                .antMatchers("/fail.html").permitAll()  // 登录失败页不需要认证
                .anyRequest().authenticated();                       // 其他所有请求都必须认证。必须登录后才能访问

        // 关闭csrf防护
        http.csrf().disable();
    }
}
