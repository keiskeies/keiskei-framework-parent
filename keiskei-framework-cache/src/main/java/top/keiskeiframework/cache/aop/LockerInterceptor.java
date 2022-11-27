package top.keiskeiframework.cache.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import top.keiskeiframework.common.annotation.annotation.Lockable;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.exception.cache.LockException;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.Lock;

/**
 * <p>
 * redis锁实现工具
 * </p>
 *
 * @author ：陈加敏 right_way@foxmail.com
 * @since ：19/12/11 0:33
 */
@Aspect
@Component
@Slf4j
public class LockerInterceptor {

    @Autowired
    private RedisLockRegistry redisLockRegistry;
    private final static int MAX_KEY_LENGTH = 128;
    private final static String TARGET_CLASS_FLAG = "targetClass";
    private final static String V_TARGET_CLASS_FLAG = "#targetClass";

    @Around(value = "@annotation(top.keiskeiframework.common.annotation.annotation.Lockable)")
    public Object aroundLock(ProceedingJoinPoint point) throws Throwable {

        Class<?> targetClass = point.getTarget().getClass();
        Method method = ((MethodSignature) point.getSignature()).getMethod();

        Lockable lockable = method.getAnnotation(Lockable.class);

        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable(TARGET_CLASS_FLAG, targetClass);
        String[] paramNames = new LocalVariableTableParameterNameDiscoverer().getParameterNames(method);
        Object[] arguments = point.getArgs();
        if (null != paramNames && null != arguments && paramNames.length == arguments.length) {
            for (int i = 0; i < arguments.length; i++) {
                context.setVariable(paramNames[i], arguments[i]);
            }
        }

        String key = lockable.key();
        if (!key.contains(V_TARGET_CLASS_FLAG) && key.contains(TARGET_CLASS_FLAG)) {
            key = key.replaceAll(TARGET_CLASS_FLAG, V_TARGET_CLASS_FLAG);
        }

        String lockKey = new SpelExpressionParser().parseExpression(key).getValue(context, String.class);
        if (StringUtils.isEmpty(lockKey)) {
            throw new LockException(BizExceptionEnum.ERROR.getCode(), "lock key error, please check" + targetClass.getName() + "." + method.getName());
        }
        if (lockKey.length() > MAX_KEY_LENGTH) {
            lockKey = DigestUtils.md5DigestAsHex(lockKey.getBytes(StandardCharsets.UTF_8));
        }

        Lock lock = redisLockRegistry.obtain(lockKey);
        if (null == lock) {
            throw new LockException(BizExceptionEnum.ERROR.getCode(), lockable.message());
        }
        boolean lockFlag;
        try {
            lockFlag = lock.tryLock(lockable.waitTime(), lockable.lockTimeUnit());
        } catch (InterruptedException e) {
            e.printStackTrace();
            unlock(lock);
            throw new LockException(BizExceptionEnum.ERROR.getCode(), lockable.message());
        }
        if (!lockFlag) {
            unlock(lock);
            throw new LockException(BizExceptionEnum.ERROR.getCode(), lockable.message());
        }
        try {
            return point.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw throwable;
        } finally {
            unlock(lock);
        }
    }

    private void unlock(Lock lock) {
        try {
            lock.unlock();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
