package top.keiskeiframework.cloud.core.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import top.keiskeiframework.cloud.core.config.TokenProperties;
import top.keiskeiframework.cloud.core.vo.TokenUser;
import top.keiskeiframework.common.util.JwtTokenUtils;
import top.keiskeiframework.common.util.MdcUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * <p>
 * 用户信息前置过滤器
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

        String userId = httpServletRequest.getHeader(MdcUtils.USER_ID);
        if (StringUtils.isEmpty(userId)) {
            String token = httpServletRequest.getHeader(tokenProperties.getTokenHeader());
            if (!StringUtils.isEmpty(token)) {
                TokenUser tokenUser = JwtTokenUtils.parse(token, TokenUser.class);
                if (null != tokenUser) {
                    MdcUtils.setUserId(tokenUser.getId() + "");
                    MdcUtils.setUserName(tokenUser.getUsername());
                    MdcUtils.setUserDepartment(tokenUser.getDepartment());
                }
            }
        } else {
            MdcUtils.setUserId(userId);
            MdcUtils.setUserName(httpServletRequest.getHeader(MdcUtils.USER_NAME));
            MdcUtils.setUserDepartment(httpServletRequest.getHeader(MdcUtils.USER_DEPARTMENT));
        }
        filterChain.doFilter(servletRequest, servletResponse);

    }
}
