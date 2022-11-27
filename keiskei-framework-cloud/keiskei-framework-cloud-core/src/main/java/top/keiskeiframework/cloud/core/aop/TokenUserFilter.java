package top.keiskeiframework.cloud.core.aop;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
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
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        setMdcValueFromHeader(MdcUtils.USER_ID, request);
        setMdcValueFromHeader(MdcUtils.USER_NAME, request);
        setMdcValueFromHeader(MdcUtils.USER_DEPARTMENT, request);
        setMdcValueFromHeader(MdcUtils.CHECK_DEPARTMENT, request);

        MdcUtils.setUserIp(getIpAddress(request));

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private static void setMdcValueFromHeader(String key, HttpServletRequest request) {
        String value = request.getHeader(key);
        if (!StringUtils.isEmpty(value)) {
            MDC.put(key, value);
        }
    }


    private final static String UN_KNOWN = "unKnown";

    /**
     * 获取请求真实IP
     *
     * @param request 。
     * @return 。
     */
    private static String getIpAddress(HttpServletRequest request) {
        String xip = request.getHeader("X-Real-IP");
        String xFor = request.getHeader("X-Forwarded-For");

        if (!StringUtils.isEmpty(xFor) && !UN_KNOWN.equalsIgnoreCase(xFor)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = xFor.indexOf(",");
            if (index != -1) {
                return xFor.substring(0, index);
            } else {
                return xFor;
            }
        }
        xFor = xip;
        if (!StringUtils.isEmpty(xFor) && !UN_KNOWN.equalsIgnoreCase(xFor)) {
            return xFor;
        }
        if (!StringUtils.isEmpty(xFor) || UN_KNOWN.equalsIgnoreCase(xFor)) {
            xFor = request.getHeader("Proxy-Client-IP");
        }
        if (!StringUtils.isEmpty(xFor) || UN_KNOWN.equalsIgnoreCase(xFor)) {
            xFor = request.getHeader("WL-Proxy-Client-IP");
        }
        if (!StringUtils.isEmpty(xFor) || UN_KNOWN.equalsIgnoreCase(xFor)) {
            xFor = request.getHeader("HTTP_CLIENT_IP");
        }
        if (!StringUtils.isEmpty(xFor) || UN_KNOWN.equalsIgnoreCase(xFor)) {
            xFor = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (!StringUtils.isEmpty(xFor) || UN_KNOWN.equalsIgnoreCase(xFor)) {
            xFor = request.getRemoteAddr();
        }
        return xFor;
    }
}
