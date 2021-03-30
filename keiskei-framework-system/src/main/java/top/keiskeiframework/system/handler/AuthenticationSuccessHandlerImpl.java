package top.keiskeiframework.system.handler;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import top.keiskeiframework.common.enums.SystemEnum;
import top.keiskeiframework.common.util.SecurityUtils;
import top.keiskeiframework.common.vo.R;
import top.keiskeiframework.common.vo.TokenGrantedAuthority;
import top.keiskeiframework.common.vo.TokenUser;
import top.keiskeiframework.system.entity.OperateLog;
import top.keiskeiframework.system.entity.User;
import top.keiskeiframework.system.enums.OperateTypeEnum;
import top.keiskeiframework.system.service.IOperateLogService;
import top.keiskeiframework.system.util.ResponseUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * <p>
 * 登录成功
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2018年10月12日 上午10:02:09
 */
@Component
@Slf4j
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    @Autowired
    private IOperateLogService operateLogService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        //获取登录成功的用户信息
        TokenUser tokenUser = (TokenUser) authentication.getPrincipal();
        log.info("用户 {} - {} 登入系统!", tokenUser.getUsername(), tokenUser.getName());


        //对前端隐藏密码
        tokenUser.setPassword(null);
        ResponseUtils.write(request, response, JSON.toJSONString(R.ok(tokenUser)));

        OperateLog operateLog = new OperateLog();
        operateLog.setIp(SecurityUtils.getIpAddress(request));
        operateLog.setType(OperateTypeEnum.LOGIN.getType());
        operateLog.setUser(User.builder().id(tokenUser.getId()).build());
        operateLogService.save(operateLog);
    }
}
