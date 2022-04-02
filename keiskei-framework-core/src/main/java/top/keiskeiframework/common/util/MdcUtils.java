package top.keiskeiframework.common.util;

import org.slf4j.MDC;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.exception.BizException;

/**
 * <p>
 * slfj日志用户信息记录
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/16 22:00
 */
public class MdcUtils {

    private static final String USER_ID = "userId";
    private static final String USER_NAME = "userName";
    private static final String USER_DEPARTMENT = "userDepartment";

    public static void setUserId(String userId) {
        MDC.put(USER_ID, userId);
    }
    public static String getUserId() {
        return MDC.get(USER_ID);
    }

    public static Long getLongUserId() {
        String mdcUserIdStr = MDC.get(USER_ID);
        if (StringUtils.isEmpty(mdcUserIdStr)) {
            throw new BizException(BizExceptionEnum.AUTH_ERROR);
        }
        return Long.parseLong(mdcUserIdStr);
    }

    public static Long getLongUserIdNoException() {
        String mdcUserIdStr = MDC.get(USER_ID);
        if (StringUtils.isEmpty(mdcUserIdStr)) {
            return null;
        }
        return Long.parseLong(mdcUserIdStr);
    }



    public static void setUserName(String userName) {
        MDC.put(USER_NAME, userName);
    }
    public static String getUserName() {
        return MDC.get(USER_NAME);
    }
    public static void setUserDepartment(String userDepartment) {
        MDC.put(USER_DEPARTMENT, userDepartment);
    }
    public static String getUserDepartment() {
        return MDC.get(USER_DEPARTMENT);
    }
}
