package top.keiskeiframework.system.config;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import top.keiskeiframework.system.properties.SystemProperties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author James Chen right_way@foxmail.com
 * @since 2020/9/25 12:29 下午
 */
@NoArgsConstructor
@AllArgsConstructor
public class RestApiInterceptor extends HandlerInterceptorAdapter {

    private SystemProperties systemProperties;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        if (systemProperties.getCross()) {
            response.setContentType("application/json;charset=utf-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Origin", request.getHeader("Origin"));
        }
        response.setContentType("application/json;charset=utf-8");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Credentials", "*");
        response.setHeader("Access-Control-Expose-Headers", "*");
        response.setHeader("Access-Control-Request-Headers", "*");
        response.setHeader("Expires", systemProperties.getRememberSeconds() + "");

        return true;
    }
}
