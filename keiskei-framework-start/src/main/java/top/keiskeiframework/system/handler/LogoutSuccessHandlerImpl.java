package top.keiskeiframework.system.handler;

import com.alibaba.fastjson.JSON;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import top.keiskeiframework.common.vo.R;
import top.keiskeiframework.system.util.ResponseUtils;
import top.keiskeiframework.system.util.SecurityUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * 退出登录成功
 * </p>
 *
 * @author 陈加敏 right_way@foxmail.com
 * @since 2019-06-26
 */
@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        SecurityUtils.removeSession(request);
        ResponseUtils.write(request, response, JSON.toJSONString(R.ok(true)));
    }
}
