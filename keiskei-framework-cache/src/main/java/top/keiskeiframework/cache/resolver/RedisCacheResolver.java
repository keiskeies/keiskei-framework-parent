package top.keiskeiframework.cache.resolver;

import org.springframework.core.convert.ConversionService;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.lang.NonNull;

/**
 * <p>
 * redis 缓存删除通配符
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/5/13 16:54
 */
public class RedisCacheResolver extends RedisCache {

    private final String name;
    private final RedisCacheWriter cacheWriter;
    private final ConversionService conversionService;
    private final static String ALL_SUFFIX = "*";

    protected RedisCacheResolver(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfig) {
        super(name, cacheWriter, cacheConfig);
        this.name = name;
        this.cacheWriter = cacheWriter;
        this.conversionService = cacheConfig.getConversionService();
    }

    @Override
    public void evict(@NonNull Object key) {
        if (key instanceof String) {
            String keyString = key.toString();
            // 后缀删除
            if (keyString.endsWith(ALL_SUFFIX)) {
                evictLikeSuffix(keyString);
                return;
            }
        }
        // 删除指定的key
        super.evict(key);
    }

    /**
     * 后缀匹配匹配
     *
     * @param key key
     */
    private void evictLikeSuffix(String key) {
        byte[] pattern = this.conversionService.convert(this.createCacheKey(key), byte[].class);
        assert pattern != null;
        this.cacheWriter.clean(this.name, pattern);
    }
}
