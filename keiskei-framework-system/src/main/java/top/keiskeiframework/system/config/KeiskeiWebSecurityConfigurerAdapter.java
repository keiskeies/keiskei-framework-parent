package top.keiskeiframework.system.config;

import top.keiskeiframework.system.handler.*;
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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().sessionManagement().maximumSessions(systemProperties.getMaximumSessions());
        if (systemProperties.getCross()) {
            http.cors().disable();
        }
        http.headers().defaultsDisabled().cacheControl();
        http.authorizeRequests().antMatchers(systemProperties.getPermitUri()).permitAll();

        http.authorizeRequests().antMatchers("/admin/**").permitAll();
//        if (!StringUtils.isEmpty(userTokenProperties.getAuthenticatedUrl())) {
//            http.authorizeRequests().antMatchers(userTokenProperties.getAuthenticatedUrl().split(",")).authenticated();
//        }


        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).accessDeniedHandler(accessDeniedHandler);
        http.formLogin().loginPage(systemProperties.getAuthWebLoginPath()).successHandler(authenticationSuccessHandler).failureHandler(authenticationFailureHandler).permitAll();
        http.logout().logoutUrl(systemProperties.getAuthWebLogoutPath()).addLogoutHandler(logoutHandlerImpl).logoutSuccessHandler(logoutSuccessHandler).permitAll();
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
