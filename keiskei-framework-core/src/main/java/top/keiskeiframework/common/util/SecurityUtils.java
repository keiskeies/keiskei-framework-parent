package top.keiskeiframework.common.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.common.vo.user.TokenUser;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * security工具类
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2018年10月12日 上午10:02:09
 */
public class SecurityUtils {
    public static TokenUser getSessionUser() {
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(obj instanceof TokenUser)) {
            throw new BizException(BizExceptionEnum.AUTH_ERROR);
        }

        return (TokenUser) obj;
    }

    public static String getDepartment() {
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(obj instanceof TokenUser)) {
            return "";
        }

        return ((TokenUser) obj).getDepartment();
    }

    private final static String UN_KNOWN = "unKnown";
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
