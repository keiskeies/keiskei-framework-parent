package top.keiskeiframework.cpreading.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.common.util.MdcUtils;
import top.keiskeiframework.cpreading.config.ApiUrlProperties;
import top.keiskeiframework.cpreading.dto.VipResponse;
import top.keiskeiframework.cpreading.util.SessionUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * controller 方法进入 日志打印
 *
 * @author James Chen right_way@foxmail.com
 * @since 2018年9月30日 下午5:11:56
 */
@Aspect
@Component
@Slf4j
public class ReaderInfoInterceptor {

    private static final String FILTER_URL_PREFIX = "/api/";
    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();
    @Autowired
    private ApiUrlProperties apiUrlProperties;


    @Pointcut("@annotation(io.swagger.annotations.ApiOperation)")
    public void pointCut() {
    }

    @Before("pointCut()")
    public void validateReader(JoinPoint joinPoint) throws Throwable {

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            String uri = request.getRequestURI();
            if (uri.startsWith(FILTER_URL_PREFIX)) {
                VipResponse user =  SessionUtils.getUserBySessionSafe();
                if (null != user) {
                    MdcUtils.setUserId(user.getId() + "");
                    MdcUtils.setUserName(user.getName());
                } else {
                    boolean authError = true;
                    for (String whiteUri : apiUrlProperties.getPermitUri()) {
                        if (ANT_PATH_MATCHER.match(whiteUri, uri)) {
                            authError = false;
                            break;
                        }
                    }
                    if (authError) {
                        throw new BizException(BizExceptionEnum.AUTH_ERROR);
                    }
                }
            }
        }
    }
}
