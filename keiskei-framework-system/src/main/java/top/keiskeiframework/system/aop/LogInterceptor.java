package top.keiskeiframework.system.aop;

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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.keiskeiframework.common.base.BaseRequest;
import top.keiskeiframework.common.enums.ApiErrorCode;
import top.keiskeiframework.common.vo.R;
import top.keiskeiframework.common.vo.TokenUser;
import top.keiskeiframework.system.entity.OperateLog;
import top.keiskeiframework.system.service.IOperateLogService;
import top.keiskeiframework.common.util.SecurityUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
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

    @Autowired
    private IOperateLogService operateLogService;

    @Pointcut("@annotation(io.swagger.annotations.ApiOperation)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object recordSysLog(ProceedingJoinPoint point) throws Throwable {

        OperateLog operateLog = new OperateLog();

        try {
            TokenUser tokenUser = SecurityUtils.getSessionUser();
            operateLog.setUserId(tokenUser.getId());
            MDC.put("mdcTraceId", tokenUser.getName() + " - " + tokenUser.getId());

        } catch (IllegalArgumentException ignored) {
        }

        try {
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            operateLog.setType(request.getMethod());
            operateLog.setIp(SecurityUtils.getIpAddress(request));
            operateLog.setUrl(request.getRequestURI());
        } catch (Exception ignored) {
        }

        try {
            MethodSignature ms = (MethodSignature) point.getSignature();
            ApiOperation annotation = ms.getMethod().getAnnotation(ApiOperation.class);
            Api api = point.getTarget().getClass().getAnnotation(Api.class);
            Object[] params = point.getArgs();

            operateLog.setName(Arrays.toString(api.tags()) + " - " + annotation.value());

            StringBuilder sb = new StringBuilder();
            for (Object param : params) {
                if (param instanceof BaseRequest) {
                    sb.append(param.toString());
                } else if (!(param instanceof HttpServletRequest) && !(param instanceof HttpServletResponse)) {
                    sb.append(JSON.toJSONString(param, SerializerFeature.IgnoreErrorGetter));
                }

            }
            operateLog.setRequestParam(sb.toString());
            log.info("{} - 开始 - 参数{}", operateLog.getName(), operateLog.getRequestParam());
        } catch (Exception e) {
            log.error("日志记录出错!", e);
        }

        Object result = null;
        try {
            result = point.proceed();
            if (result instanceof R) {
                R r = (R) result;
                r.setMsg(operateLog.getName() + ApiErrorCode.fromCode(r.getCode()).getMsg());
                return r;
            }
            return result;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw throwable;
        } finally {
            try {
                if (result != null) {
                    operateLog.setResponseParam(JSON.toJSONString(result, SerializerFeature.IgnoreErrorGetter));
                    log.info("{} -结束 - 返回结果:{}", operateLog.getName(), operateLog.getResponseParam());
                } else {
                    log.info("{} -结束", operateLog.getName());
                }
            } catch (Exception e) {
                log.error("日志记录出错!", e);
            }
            MDC.clear();
            operateLogService.saveAsync(operateLog);
        }
    }


}
