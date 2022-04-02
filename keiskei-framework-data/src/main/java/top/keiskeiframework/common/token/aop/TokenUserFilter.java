package top.keiskeiframework.common.token.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.token.config.TokenProperties;
import top.keiskeiframework.common.token.vo.TokenUser;
import top.keiskeiframework.common.util.JwtTokenUtils;
import top.keiskeiframework.common.util.MdcUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

/**
 * <p>
 *
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/3/11 14:11
 */
@Slf4j
@WebFilter(urlPatterns = "/*", filterName = "tokenUserFilter")
@Component
public class TokenUserFilter implements Filter {

    @Autowired
    private TokenProperties tokenProperties;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        Enumeration<String> tokenHeaders = httpServletRequest.getHeaders(tokenProperties.getTokenHeader());
        if (null != tokenHeaders && tokenHeaders.hasMoreElements()) {
            String token =  httpServletRequest.getHeaders(tokenProperties.getTokenHeader()).nextElement();
            if (!StringUtils.isEmpty(token)) {
                TokenUser tokenUser = JwtTokenUtils.parse(token, TokenUser.class);
                if (null != tokenUser) {
                    MdcUtils.setUserId(tokenUser.getId() + "");
                    MdcUtils.setUserName(tokenUser.getUsername());
                }
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);

    }
}
