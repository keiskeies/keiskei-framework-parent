package top.keiskeiframework.system.handler;

import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.vo.R;
import com.alibaba.fastjson.JSON;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import top.keiskeiframework.system.util.ResponseUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * <p>
 * 权限认证失败
 * </p>
 *
 * @author 陈加敏 right_way@foxmail.com
 * @since 2019-06-26
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        ResponseUtils.write(request, response, JSON.toJSONString(R.failed(BizExceptionEnum.AUTH_ERROR)));
    }

}
