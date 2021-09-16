package top.keiskeiframework.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * <p>
 * 访问计数器实现工具
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2021/3/15 19:57
 */
@Aspect
@Configuration
@Slf4j
public class CountableInterceptor {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
}
