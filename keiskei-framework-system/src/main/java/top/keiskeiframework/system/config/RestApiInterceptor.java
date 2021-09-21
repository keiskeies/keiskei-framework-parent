package top.keiskeiframework.system.config;

import org.springframework.lang.NonNull;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import top.keiskeiframework.common.util.ResponseUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author James Chen right_way@foxmail.com
 * @since 2020/9/25 12:29 下午
 */
public class RestApiInterceptor extends HandlerInterceptorAdapter {


    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        ResponseUtils.confirm(request, response);
        return true;
    }
}
