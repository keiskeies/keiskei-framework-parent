package top.keiskeiframework.cache.redis.config;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.integration.redis.util.RedisLockRegistry;
import top.keiskeiframework.cache.config.HibernateCollectionMixIn;
import top.keiskeiframework.cache.enums.CacheTimeEnum;

import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * redis缓存管理工具
 *
 * @author : 陈加敏
 * @since : 2019/7/13 16:14
 */
@Configuration
@Slf4j
public class RedisCacheStorageConfiguration extends CachingConfigurerSupport {



    @Bean
    public RedisLockRegistry redisLockRegistry(RedisConnectionFactory redisConnectionFactory) {
        return new RedisLockRegistry(redisConnectionFactory, "LOCK", 5 * 60 * 1000);
    }

    /**
     * 缓存管理
     *
     * @param connectionFactory .
     * @return .
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        RedisCacheManager cacheManager = RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(getDefaultCacheConfiguration(-1))
                .withInitialCacheConfigurations(getCacheConfigurations())
                .transactionAware()
                .build();

        log.info("RedisCacheManager loading success");
        return cacheManager;
    }

    /**
     * 针对key名称设置时效
     *
     * @return 。
     */
    private Map<String, RedisCacheConfiguration> getCacheConfigurations() {
        Map<String, RedisCacheConfiguration> configurationMap = new HashMap<>(16);
        configurationMap.put(CacheTimeEnum.M1, this.getDefaultCacheConfiguration(60));
        configurationMap.put(CacheTimeEnum.M5, this.getDefaultCacheConfiguration(5 * 60));
        configurationMap.put(CacheTimeEnum.M10, this.getDefaultCacheConfiguration(10 * 60));
        configurationMap.put(CacheTimeEnum.M30, this.getDefaultCacheConfiguration(30 * 60));
        configurationMap.put(CacheTimeEnum.H1, this.getDefaultCacheConfiguration(60 * 60));
        configurationMap.put(CacheTimeEnum.H2, this.getDefaultCacheConfiguration(2 * 60 * 60));
        configurationMap.put(CacheTimeEnum.H6, this.getDefaultCacheConfiguration(6 * 60 * 60));
        configurationMap.put(CacheTimeEnum.H12, this.getDefaultCacheConfiguration(12 * 60 * 60));
        configurationMap.put(CacheTimeEnum.D1, this.getDefaultCacheConfiguration(24 * 60 * 60));
        configurationMap.put(CacheTimeEnum.D2, this.getDefaultCacheConfiguration(2 * 24 * 60 * 60));
        configurationMap.put(CacheTimeEnum.D5, this.getDefaultCacheConfiguration(5 * 24 * 60 * 60));
        configurationMap.put(CacheTimeEnum.D7, this.getDefaultCacheConfiguration(7 * 24 * 60 * 60));

        return configurationMap;
    }

    /**
     * 获取redis的缓存配置(针对于键)
     *
     * @param seconds 键过期时间
     * @return .
     */
    private RedisCacheConfiguration getDefaultCacheConfiguration(long seconds) {
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        om.addMixIn(Collection.class, HibernateCollectionMixIn.class);

        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        return RedisCacheConfiguration
                .defaultCacheConfig()
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                .entryTtl(Duration.ofSeconds(seconds));
    }

    /**
     * @param redisConnectionFactory .
     * @return .
     */
    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        log.info("RedisTemplate loading success ");
        return redisTemplate;
    }
}
