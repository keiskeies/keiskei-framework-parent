package top.keiskeiframework.cpreading.reader.util;

import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.keiskeiframework.cache.service.CacheStorageService;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.common.util.SpringUtils;
import top.keiskeiframework.cpreading.reader.dto.VipResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * <p>
 *
 * </p>
 *
 * @author ：陈加敏 right_way@foxmail.com
 * @since ：2019/12/19 10:22
 */
public class SessionUtils {

    private final static String SESSION_PREFIX = "spring:session:sessions:%s";
    private static final String HEADER_X_AUTH_TOKEN = "X-Auth-Token";
    private final static String USER_FLAG = "user";

    public static VipResponse getUserBySession() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            throw new BizException(BizExceptionEnum.AUTH_ERROR);
        }
        //获取本地线程绑定的请求对象
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        if (StringUtils.isEmpty(request.getHeader(HEADER_X_AUTH_TOKEN))) {
            throw new BizException(BizExceptionEnum.AUTH_ERROR);
        }
        HttpSession session = request.getSession();

        VipResponse user = (VipResponse) session.getAttribute(USER_FLAG);

        if (user == null) {
            throw new BizException(BizExceptionEnum.AUTH_ERROR);
        }
        user.setIp(getIpAddress(request));
        return user;
    }

    public static VipResponse getUserBySessionSafe() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            if (!StringUtils.isEmpty(request.getHeader(HEADER_X_AUTH_TOKEN))) {
                HttpSession session = request.getSession();
                VipResponse user = (VipResponse) session.getAttribute(USER_FLAG);
                if (user != null) {
                    user.setIp(getIpAddress(request));
                    return user;
                }
            }
        }
        return null;
    }


    public static String setUserSession(VipResponse user) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            throw new BizException(BizExceptionEnum.ERROR);
        }
        //获取本地线程绑定的请求对象
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        HttpSession session = request.getSession();
        session.setAttribute(USER_FLAG, user);
        return session.getId();
    }

    public static VipResponse getUserByToken(String token) {
        CacheStorageService cacheStorageService = SpringUtils.getBean(CacheStorageService.class);
        return (VipResponse) cacheStorageService.get(String.format(SESSION_PREFIX, token));
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
