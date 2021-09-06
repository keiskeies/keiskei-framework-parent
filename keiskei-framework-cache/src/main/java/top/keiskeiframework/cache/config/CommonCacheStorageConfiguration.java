package top.keiskeiframework.cache.config;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.keiskeiframework.cache.service.CacheStorageService;
import top.keiskeiframework.cache.service.impl.AbstractCacheStorageServiceImpl;

import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/3 16:45
 */
@Configuration
public class CommonCacheStorageConfiguration {

    @Autowired(required = false)
    private CacheStorageService cacheStorageService;

    public final static String KEY_GENERATOR_BEAN = "top.keiskeiframework.common.keyGenerator";



    /**
     * key 生成器
     * <p>
     * 注意: 该方法只是声明了key的生成策略,还未被使用,需在@Cacheable注解中指定keyGenerator
     * 如: @Cacheable(value = "key", keyGenerator = "cacheKeyGenerator")
     *
     * @return .
     */
    @Bean(name = KEY_GENERATOR_BEAN)
    public KeyGenerator cacheKeyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder(target.getClass().getName());
            for (Object param : params) {
                sb.append('.');
                sb.append(JSON.toJSONString(param));
            }
            return sb.toString();
        };
    }

    @Bean("cacheStorageServiceFile")
    public CacheStorageService cacheStorageService() {
        if (null == cacheStorageService) {
            cacheStorageService = new AbstractCacheStorageServiceImpl(new ConcurrentHashMap<>(16));
        }
        return cacheStorageService;

    }
}
