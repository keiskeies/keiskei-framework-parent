package top.keiskeiframework.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.util.DigestUtils;
import top.keiskeiframework.common.annotation.Lockable;
import top.keiskeiframework.common.enums.SystemEnum;
import top.keiskeiframework.common.enums.exception.BizExceptionEnum;
import top.keiskeiframework.common.exception.BizException;
import top.keiskeiframework.common.exception.LockException;

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
@Configuration
@Slf4j
public class LockerInterceptor {

    @Autowired
    private RedisLockRegistry redisLockRegistry;

    private final static int MAX_KEY_LENGTH = 64;

    @Around(value = "@annotation(top.keiskeiframework.common.annotation.Lockable)")
    public Object aroundLock(ProceedingJoinPoint point) throws Throwable {
        String key;
        Object[] arguments = point.getArgs();

        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Lockable lockable = method.getAnnotation(Lockable.class);

        key = lockable.key();
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = u.getParameterNames(method);
        if (null != paramNames) {
            ExpressionParser parser = new SpelExpressionParser();
            Expression expression = parser.parseExpression(key);
            EvaluationContext context = new StandardEvaluationContext();
            for (int i = 0; i < arguments.length; i++) {
                context.setVariable(paramNames[i], arguments[i]);
            }
            String tempKey = expression.getValue(context, String.class);
            assert tempKey != null;
            if (tempKey.length() > MAX_KEY_LENGTH) {
                tempKey = DigestUtils.md5DigestAsHex(tempKey.getBytes(StandardCharsets.UTF_8));
            }
            key = point.getTarget().getClass().getName() + SystemEnum.CACHE_SPLIT + tempKey;
        }
        Lock lock = redisLockRegistry.obtain(key);
        if (null == lock) {
            throw new LockException(BizExceptionEnum.ERROR.getCode(), lockable.message());
        }

        boolean lockFlag;
        try {
            lockFlag = lock.tryLock(lockable.waitTime(), lockable.lockTimeUnit());
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new LockException(BizExceptionEnum.ERROR.getCode(), lockable.message());
        }
        if (!lockFlag) {
            try {
                lock.unlock();
            } catch (Exception ignore) {
            }
            throw new LockException(BizExceptionEnum.ERROR.getCode(), lockable.message());
        }
        try {
            return point.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw throwable;
        } finally {
            try {
                lock.unlock();
            } catch (Exception ignore) {
            }
        }
    }
}
