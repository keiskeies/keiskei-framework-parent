package top.keiskeiframework.cache.resolver;

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.lang.NonNull;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * redis 缓存配置
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/5/13 16:55
 */
public class RedisCacheManagerResolver extends RedisCacheManager {
    private final RedisCacheWriter cacheWriter;
    private final RedisCacheConfiguration defaultCacheConfig;

    public RedisCacheManagerResolver(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfiguration;
    }

    public RedisCacheManagerResolver(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, String... initialCacheNames) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheNames);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfiguration;
    }

    public RedisCacheManagerResolver(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, boolean allowInFlightCacheCreation, String... initialCacheNames) {
        super(cacheWriter, defaultCacheConfiguration, allowInFlightCacheCreation, initialCacheNames);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfiguration;
    }

    public RedisCacheManagerResolver(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, Map<String, RedisCacheConfiguration> initialCacheConfigurations) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfiguration;
    }

    public RedisCacheManagerResolver(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration, Map<String, RedisCacheConfiguration> initialCacheConfigurations, boolean allowInFlightCacheCreation) {
        super(cacheWriter, defaultCacheConfiguration, initialCacheConfigurations, allowInFlightCacheCreation);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfig = defaultCacheConfiguration;
    }

    public RedisCacheManagerResolver(RedisConnectionFactory redisConnectionFactory, RedisCacheConfiguration cacheConfiguration) {
        this(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory), cacheConfiguration);
    }

    /**
     * 覆盖父类创建RedisCache，采用自定义的RedisCacheResolver
     *
     * @param name        name
     * @param cacheConfig cacheConfig
     * @return .
     */
    @Override
    @NonNull
    protected RedisCache createRedisCache(@NonNull String name, @Nullable RedisCacheConfiguration cacheConfig) {
        return new RedisCacheResolver(name, cacheWriter, cacheConfig != null ? cacheConfig : defaultCacheConfig);
    }

    @Override
    @NonNull
    public Map<String, RedisCacheConfiguration> getCacheConfigurations() {
        Map<String, RedisCacheConfiguration> configurationMap = new HashMap<>(getCacheNames().size());
        getCacheNames().forEach(it -> {
            RedisCache cache = (RedisCacheResolver) lookupCache(it);
            configurationMap.put(it, cache != null ? cache.getCacheConfiguration() : null);
        });
        return Collections.unmodifiableMap(configurationMap);
    }
}
