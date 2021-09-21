package top.keiskeiframework.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * 相应工具类
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2021/1/29 16:04
 */
@Slf4j
@Component
public class ResponseUtils {
    private static boolean cross = false;

    @Value("${keiskei.cross:false}")
    public void setCross(Boolean cross) {
        ResponseUtils.cross = cross;
    }



    public static void write(HttpServletRequest request, HttpServletResponse response, String message) throws IOException {
        confirm(request, response);
        response.getWriter().write(message);
    }

    public static void confirm(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json;charset=utf-8");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        if (cross) {
            response.setHeader("Access-Control-Allow-Headers", "*");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Origin", request.getHeader("Origin"));
        }
    }
}
