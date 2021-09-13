package top.keiskeiframework.system.util;

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
public class ResponseUtils {
    public static boolean cross = false;

    public static void write(HttpServletRequest request, HttpServletResponse response, String message) throws IOException {
        if (cross) {
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PATCH");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "*");
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(message);
    }
}
