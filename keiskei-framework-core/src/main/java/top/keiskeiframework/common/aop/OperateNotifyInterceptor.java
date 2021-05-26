package top.keiskeiframework.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import top.keiskeiframework.common.annotation.notify.OperateNotify;
import top.keiskeiframework.common.base.service.OperateNotifyService;

import java.lang.reflect.Method;

/**
 * <p>
 * 操作提醒
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/5/18 17:09
 */
@Aspect
@Configuration
@Slf4j
public class OperateNotifyInterceptor {

    @Autowired(required = false)
    private OperateNotifyService operateNotifyService;

    @Around(value = "@annotation(top.keiskeiframework.common.annotation.notify.OperateNotify)")
    public Object aroundLock(ProceedingJoinPoint point) throws Throwable {


        Object result = point.proceed();
        try {
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();
            OperateNotify operate = method.getAnnotation(OperateNotify.class);
            switch (operate.type()) {
                case SAVE:
                    operateNotifyService.save(result, point.getTarget());
                    break;
                case UPDATE:
                    operateNotifyService.update(result, point.getTarget());
                    break;
                case DELETE:
                    operateNotifyService.delete(result, point.getTarget());
                    break;
                default:
                    break;
            }
        } catch (Exception ignored) {
        }
        return result;
    }
}