package top.keiskeiframework.system.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.system.vo.TokenUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * security工具类
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2018年10月12日 上午10:02:09
 */
public class SecurityUtils {

    private static final BCryptPasswordEncoder B_CRYPT_PASSWORD_ENCODER = new BCryptPasswordEncoder();
    public final static String USER_TOKEN_KEY = "user";

    public static TokenUser getSessionUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (null != securityContext) {
            Authentication authentication = securityContext.getAuthentication();
            if (null != authentication) {
                Object obj = authentication.getPrincipal();
                if (obj instanceof TokenUser) {
                    return (TokenUser) obj;
                }

            }
        }

        throw new BizException(BizExceptionEnum.AUTH_ERROR);
    }

    public static TokenUser getSessionUser(HttpServletRequest request) {
        return (TokenUser) request.getSession().getAttribute(USER_TOKEN_KEY);
    }

    public static void saveSession(HttpServletRequest request, TokenUser tokenUser) {
        request.getSession().setAttribute(USER_TOKEN_KEY, tokenUser);
    }

    public static void removeSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
    }

    public static boolean matchPassword(String password, String encodePassword) {
        return B_CRYPT_PASSWORD_ENCODER.matches(password, encodePassword);
    }

    public static String encodePassword(String password) {
        return B_CRYPT_PASSWORD_ENCODER.encode(password);
    }


    private final static String UN_KNOWN = "unKnown";

    /**
     * 获取请求IP
     *
     * @param request HttpServletRequest
     * @return IP
     */
    public static String getIpAddress(HttpServletRequest request) {
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
