package top.keiskeiframework.system.config;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.common.util.VerificationCodeUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * <p>
 * 验证码错误
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/11/20 03:40
 */

public class VerifyCodeDaoAuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        String verifyCode = request.getParameter("verifyCode");
        String verifyUuid = request.getParameter("verifyUuid");
        try {
            VerificationCodeUtil.judgeCode(verifyUuid, verifyCode);
            return super.authenticate(authentication);
        } catch (BizException e) {
            throw new AuthenticationServiceException(e.getMessage());
        }
    }
}

