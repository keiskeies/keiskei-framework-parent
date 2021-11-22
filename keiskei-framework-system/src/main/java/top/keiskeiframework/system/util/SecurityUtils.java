package top.keiskeiframework.system.util;

import org.springframework.security.core.context.SecurityContextHolder;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.system.vo.user.TokenUser;

/**
 * <p>
 * security工具类
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2018年10月12日 上午10:02:09
 */
public class SecurityUtils {
    /**
     * 获取当前登陆用户
     * @return 。
     */
    public static TokenUser getSessionUser() {
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(obj instanceof TokenUser)) {
            throw new BizException(BizExceptionEnum.AUTH_ERROR);
        }
        return (TokenUser) obj;
    }



}
