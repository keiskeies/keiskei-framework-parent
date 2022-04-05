package top.keiskeiframework.cache.config;

import com.alibaba.fastjson.JSON;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 缓存KEY生成规则配置
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/9/17 00:06
 */
@Configuration
public class CommonCacheStorageConfiguration {

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
}
