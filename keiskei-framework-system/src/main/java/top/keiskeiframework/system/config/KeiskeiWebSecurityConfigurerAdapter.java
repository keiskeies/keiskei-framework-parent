package top.keiskeiframework.system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.keiskeiframework.system.handler.*;
import top.keiskeiframework.system.properties.AuthenticateUrl;
import top.keiskeiframework.system.properties.SystemProperties;
import top.keiskeiframework.system.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * springSecurity配置中心
 *
 * @author 陈加敏
 * @since 2019/7/15 13:43
 */
@Configuration
@EnableWebSecurity
public class KeiskeiWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    @Autowired
    private LogoutSuccessHandlerImpl logoutSuccessHandler;
    @Autowired
    private AccessDeniedHandlerImpl accessDeniedHandler;
    @Autowired
    private AuthenticationFailureHandlerImpl authenticationFailureHandler;
    @Autowired
    private AuthenticationSuccessHandlerImpl authenticationSuccessHandler;
    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private LogoutHandlerImpl logoutHandlerImpl;
    @Autowired
    private SystemProperties systemProperties;
    @Autowired
    private IUserService userService;
    @Value("${keiskei.use-permission:false}")
    private Boolean usePermission;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().sessionManagement().maximumSessions(systemProperties.getMaximumSessions());
        if (systemProperties.getCross()) {
            http.cors().disable();
        }
        http.headers().defaultsDisabled().cacheControl();

        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).accessDeniedHandler(accessDeniedHandler);
        http.formLogin().loginPage(systemProperties.getAuthWebLoginPath())
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .permitAll();
        http.logout().logoutUrl(systemProperties.getAuthWebLogoutPath()).
                addLogoutHandler(logoutHandlerImpl)
                .logoutSuccessHandler(logoutSuccessHandler)
                .permitAll();

        // 不进行拦截的路径
        http.authorizeRequests().antMatchers("/api/**").permitAll();
        if (null != systemProperties.getPermitUri()) {
            http.authorizeRequests().antMatchers(systemProperties.getPermitUri()).permitAll();
        }

        // 登陆后可访问的路径
        http.authorizeRequests().antMatchers("/admin/*/system/self/**").authenticated();
        if (!CollectionUtils.isEmpty(systemProperties.getAuthenticatedUrls())) {
            for (AuthenticateUrl authenticateUrl : systemProperties.getAuthenticatedUrls()) {
                http.authorizeRequests().antMatchers(authenticateUrl.getMethod(), authenticateUrl.getPath()).authenticated();
            }
        }

        if (usePermission) {
            //开启自定义连接拦截
            http.authorizeRequests().anyRequest().access("@rbacAuthorityService.hasPermission(request,authentication)");
        }
        http.rememberMe().rememberMeParameter("remember-me").tokenValiditySeconds(systemProperties.getRememberSeconds());
        http.authorizeRequests().antMatchers("/**").authenticated();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setHideUserNotFoundExceptions(false);
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder());
        return provider;
    }

}
