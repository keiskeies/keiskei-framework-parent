package top.keiskeiframework.system.handler;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.vo.R;
import top.keiskeiframework.system.properties.SystemProperties;
import top.keiskeiframework.system.service.IUserService;
import top.keiskeiframework.system.util.ResponseUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * <p>
 * 登录失败
 * </p>
 *
 * @author 陈加敏 right_way@foxmail.com
 * @since 2019-06-26
 */
@Component
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {


    @Autowired
    private IUserService userService;
    @Autowired
    private SystemProperties systemProperties;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        R<?> r;

        if (exception instanceof AccountExpiredException) {
            r = R.failed(BizExceptionEnum.AUTH_ACCOUNT_EXPIRED);
        } else if (exception instanceof CredentialsExpiredException) {
            r = R.failed(BizExceptionEnum.AUTH_PASSWORD_EXPIRED);
        } else if (exception instanceof UsernameNotFoundException) {
            r = R.failed(BizExceptionEnum.AUTH_USER_NOT_FOND);
        } else if (exception instanceof LockedException) {
            r = R.failed(BizExceptionEnum.AUTH_ACCOUNT_LOCKED);
        } else if (exception instanceof AuthenticationServiceException) {
            r = R.failed(BizExceptionEnum.VERIFY_CODE_ERROR.getCode(), exception.getMessage());
        } else if (exception instanceof BadCredentialsException) {
            r = R.failed(BizExceptionEnum.AUTH_PASSWORD_ERROR);
            String username = request.getParameter("username");
            Long passwordErrorTimes = userService.addPasswordErrorTimes(username);
            r.setMsg("密码错误" + passwordErrorTimes + "次，达到" + systemProperties.getPasswordErrorTimesMax() + "次账号将被锁定");

        } else {
            r = R.failed(exception.getMessage());
        }
        ResponseUtils.write(request, response, JSON.toJSONString(r));
    }
}
