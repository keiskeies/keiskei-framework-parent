package top.keiskeiframework.common.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.keiskeiframework.common.base.dto.BaseRequestVO;
import top.keiskeiframework.common.base.service.OperateLogService;
import top.keiskeiframework.common.dto.log.OperateLogDTO;
import top.keiskeiframework.common.enums.exception.ApiErrorCode;
import top.keiskeiframework.common.util.MdcUtils;
import top.keiskeiframework.common.util.ThreadPoolExecUtil;
import top.keiskeiframework.common.util.UserInfoUtils;
import top.keiskeiframework.common.vo.R;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * controller 方法进入 日志打印
 *
 * @author James Chen right_way@foxmail.com
 * @since 2018年9月30日 下午5:11:56
 */
@Aspect
@Component
@Slf4j
public class LogInterceptor {

    @Autowired(required = false)
    private OperateLogService operateLogService;
    private final static String LOG_NAME_FORMATTER = "[%s-%s]";
    private final static String MESSAGE_FORMATTER = "%s-%s";
    private final static String GET = "GET";

    @Pointcut("@annotation(io.swagger.annotations.ApiOperation)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object recordSysLog(ProceedingJoinPoint point) throws Throwable {
        long start = System.currentTimeMillis();
        OperateLogDTO operateLog = new OperateLogDTO();
        try {
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            String userId = MdcUtils.getUserId();
            if (!StringUtils.isEmpty(userId)) {
                operateLog.setUserId(Long.valueOf(MdcUtils.getUserId()));
            }
            operateLog.setType(request.getMethod());
            operateLog.setIp(UserInfoUtils.getIpAddress(request));
            operateLog.setUrl(request.getRequestURI());

            MethodSignature ms = (MethodSignature) point.getSignature();
            ApiOperation annotation = ms.getMethod().getAnnotation(ApiOperation.class);
            Api api = point.getTarget().getClass().getAnnotation(Api.class);
            Object[] params = point.getArgs();

            operateLog.setName(String.format(LOG_NAME_FORMATTER, String.join(",", api.tags()), annotation.value()));

            StringBuilder sb = new StringBuilder();
            for (Object param : params) {
                if (param instanceof BaseRequestVO) {
                    sb.append(param);
                } else if (!(param instanceof HttpServletRequest) && !(param instanceof HttpServletResponse)) {
                    sb.append(JSON.toJSONString(param, SerializerFeature.IgnoreErrorGetter));
                }
            }
            operateLog.setRequestParam(sb.toString());
            log.info("{} - start - params: \n{}", operateLog.getName(), operateLog.getRequestParam());
        } catch (Exception e) {
            log.error("log error!", e);
        }

        Object result = null;
        try {
            result = point.proceed();
            if (result instanceof R) {
                R<?> r = (R<?>) result;
                r.setMsg(String.format(MESSAGE_FORMATTER, operateLog.getName(), ApiErrorCode.fromCode(r.getCode()).getMsg()));
                return r;
            }
            return result;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw throwable;
        } finally {
            try {
                long end = System.currentTimeMillis();
                if (null != result) {
                    String responseParam = JSON.toJSONString(result, SerializerFeature.IgnoreErrorGetter);
                    if (!GET.equalsIgnoreCase(operateLog.getType())) {
                        operateLog.setResponseParam(responseParam);
                    }
                    log.info("{} - end - timer: {}- result: \n{}", operateLog.getName(), end - start, responseParam);
                } else {
                    log.info("{} - end - timer: {}", operateLog.getName(), end - start);
                }
            } catch (Exception e) {
                log.error("log error!", e);
            } finally {
                MDC.clear();
                if (null != operateLogService) {
                    ThreadPoolExecUtil.execute(() -> {
                        operateLogService.saveLog(operateLog);
                    });
                }
            }
        }
    }
}
