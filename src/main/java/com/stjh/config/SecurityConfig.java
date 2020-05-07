package com.stjh.config;

import com.stjh.handler.MyAccessDeniedHandler;
import com.stjh.handler.MyAuthenticationFailureHandler;
import com.stjh.handler.MyAuthenticationSuccessHandler;
import com.stjh.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * Security配置
 * @author wuge
 * @date 2020/4/28 22:47
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 注入MyAccessDeniedHandler 自定义403处理
     */
    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;

    /**
     * 注入UserDetailsServiceImpl
     */
    private UserDetailsServiceImpl userDetailsService;

    /**
     * 注入DataSource
     */
    @Autowired
    private DataSource dataSource;

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
                //.antMatchers("/css/**", "/js/**", "/img/**").permitAll()
                .antMatchers("/**/*.png").permitAll()
                //.regexMatchers(HttpMethod.GET, "/demo").permitAll() // 匹配请求方式（可选参数）和正则表达式
                //.mvcMatchers("/demo").servletPath("/stjh").permitAll()
                .antMatchers("/main1.html").hasAnyAuthority("admin") // 权限
                .antMatchers("/main1.html").hasRole("abc") // 角色，此处不能以ROLE_abc，启动会报错
                //.antMatchers("/main1.html").hasIpAddress("127.0.0.1") // IP地址不是也不能访问
                .anyRequest().authenticated();                       // 其他所有请求都必须认证。必须登录后才能访问
                //.anyRequest().access("@userServiceImpl.hasPermission(request, authentication)"); // 自定义方法实现权限控制

        // 关闭csrf防护
        http.csrf().disable();

        // 403异常处理
        http.exceptionHandling()
                .accessDeniedHandler(myAccessDeniedHandler);

        // 记住我
        http.rememberMe()
                .tokenValiditySeconds(10)  //记住我过期时间 单位秒 不写默认两周
                //.rememberMeParameter("rememberMe") //form表单：记住我 name，默认remember-me
                .userDetailsService(userDetailsService)  // 用户登录逻辑
                .tokenRepository(getPersistentTokenRepository());
        // 可以用and()连接这五部分代码
    }

    /**
     * 设置rememberMe
     * @return
     */
    @Bean
    public PersistentTokenRepository getPersistentTokenRepository() {
        JdbcTokenRepositoryImpl impl = new JdbcTokenRepositoryImpl();
        // 设置数据源存放rememberMe持久化信息
        impl.setDataSource(dataSource);
        // 设置框架自动生成rememberMe相关表，第二次启动时要注释掉此行代码
        //impl.setCreateTableOnStartup(Boolean.TRUE);
        return impl;
    }
}
