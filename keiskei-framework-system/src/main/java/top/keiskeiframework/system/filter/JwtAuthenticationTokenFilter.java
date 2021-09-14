package top.keiskeiframework.system.filter;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.util.JwtTokenUtils;
import top.keiskeiframework.common.vo.R;
import top.keiskeiframework.system.util.ResponseUtils;
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
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private static final String OPTIONS = "OPTIONS";
    private static final String ACCESS_TOKEN = "Access-Token";


    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
            throws ServletException, IOException {
        //初始化请求头

        if (OPTIONS.equalsIgnoreCase(request.getMethod())) {
            ResponseUtils.write(request, response, JSON.toJSONString(R.failed(BizExceptionEnum.AUTH_ERROR)));
            return;
        }
        //获取请求token
        String authToken = request.getHeader(ACCESS_TOKEN);
        if (!StringUtils.isEmpty(authToken)) {
            TokenUser tokenUser = JwtTokenUtils.parse(authToken, TokenUser.class);
            if (null != tokenUser) {
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            tokenUser,
                            tokenUser.getPassword(),
                            tokenUser.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } else {
                //向前端发送错误信息
                ResponseUtils.write(request, response, JSON.toJSONString(R.failed(BizExceptionEnum.AUTH_ERROR)));
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
