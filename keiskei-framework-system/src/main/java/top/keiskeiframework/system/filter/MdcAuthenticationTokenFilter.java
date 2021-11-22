package top.keiskeiframework.system.filter;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import top.keiskeiframework.common.util.MdcUtils;
import top.keiskeiframework.common.util.ResponseUtils;
import top.keiskeiframework.common.vo.R;
import top.keiskeiframework.system.util.SecurityUtils;
import top.keiskeiframework.system.vo.user.TokenUser;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 通过Token从Redis缓存中获取用户信息
 *
 * @author 陈加敏
 * @since 2019/7/15 13:56
 */
@Slf4j
@Component
public class MdcAuthenticationTokenFilter extends OncePerRequestFilter {

    private static final String OPTIONS = "OPTIONS";


    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
            throws ServletException, IOException {
        //初始化请求头

        if (OPTIONS.equalsIgnoreCase(request.getMethod())) {
            ResponseUtils.write(request, response, JSON.toJSONString(R.ok(true)));
            return;
        }
        TokenUser tokenUser = SecurityUtils.getSessionUser();
        MdcUtils.setUserId(tokenUser.getId());
        MdcUtils.setUserName(tokenUser.getName());
        MdcUtils.setUserDepartment(tokenUser.getDepartment());

        chain.doFilter(request, response);
    }
}
