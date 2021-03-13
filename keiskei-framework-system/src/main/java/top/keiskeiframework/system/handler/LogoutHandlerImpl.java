package top.keiskeiframework.system.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * 退出登录
 * </p>
 *
 * @author 陈加敏 right_way@foxmail.com
 * @since 2019-06-26
 */
@Component
@Slf4j
public class LogoutHandlerImpl implements LogoutHandler {


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        HttpSession session = request.getSession();
        session.invalidate();
    }
}
