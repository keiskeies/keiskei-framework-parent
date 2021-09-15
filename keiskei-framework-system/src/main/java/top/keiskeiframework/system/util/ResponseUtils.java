package top.keiskeiframework.system.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * <p>
 * 相应工具类
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2021/1/29 16:04
 */
@Slf4j
public class ResponseUtils {
    public static boolean cross = false;

    public static void write(HttpServletRequest request, HttpServletResponse response, String message) throws IOException {
        confirm(request, response);
        response.getWriter().write(message);
    }

    public static void confirm(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json;charset=utf-8");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PATCH, PUT");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        if (cross) {
            response.setHeader("Access-Control-Allow-Headers", "*");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Origin", request.getHeader("Origin"));
        }
    }
}
