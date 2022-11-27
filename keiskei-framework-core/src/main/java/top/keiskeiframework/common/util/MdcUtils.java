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

    public static final String USER_ID = "userId";
    public static final String USER_IP = "userIp";
    public static final String USER_NAME = "userName";
    public static final String USER_DEPARTMENT = "userDepartment";
    public static final String CHECK_DEPARTMENT = "checkDepartment";

    private static final String TRUE_STR = "true";

    public static void setUserId(String userId) {
        MDC.put(USER_ID, userId);
    }

    public static String getUserId() {
        return MDC.get(USER_ID);
    }

    public static void setUserIp(String userIp) {
        MDC.put(USER_IP, userIp);
    }

    public static String getUserIp() {
        return MDC.get(USER_IP);
    }

    public static String getStringUserId() {
        String mdcUserIdStr = MDC.get(USER_ID);
        if (StringUtils.isEmpty(mdcUserIdStr)) {
            throw new BizException(BizExceptionEnum.AUTH_ERROR);
        }
        return mdcUserIdStr;
    }

    public static Integer getIntegerUserId() {
        String mdcUserIdStr = MDC.get(USER_ID);
        if (StringUtils.isEmpty(mdcUserIdStr)) {
            throw new BizException(BizExceptionEnum.AUTH_ERROR);
        }
        return Integer.parseInt(mdcUserIdStr);
    }

    public static Integer getIntegerUserIdNoException() {
        String mdcUserIdStr = MDC.get(USER_ID);
        if (StringUtils.isEmpty(mdcUserIdStr)) {
            return null;
        }
        return Integer.parseInt(mdcUserIdStr);
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
        return MDC.get(USER_DEPARTMENT) + "";
    }

    public static void setCheckDepartment(String checkDepartment) {
        MDC.put(CHECK_DEPARTMENT, checkDepartment );
    }

    public static String getCheckDepartment() {
        return MDC.get(CHECK_DEPARTMENT);
    }
    public static boolean checkDepartment() {
        return TRUE_STR.equals(MDC.get(CHECK_DEPARTMENT));
    }
}
