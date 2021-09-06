package top.keiskeiframework.cache.simple.config;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * jcache缓存管理工具
 *
 * @author : 陈加敏
 * @since : 2019/7/13 16:14
 */
@Configuration
@EnableCaching
@Slf4j
public class SimpleCacheStorageConfiguration extends CachingConfigurerSupport {

//    @Autowired
//    private CacheExpiredProperties cacheExpiredProperties;





//
//    @Bean
//    public RedisLockRegistry jcacheLockRegistry(RedisConnectionFactory jcacheConnectionFactory) {
//        return new RedisLockRegistry(jcacheConnectionFactory, "LOCK", 5 * 60 * 1000);
//    }
//
//

//
//    /**
//     * 针对key名称设置时效
//     *
//     * @return 。
//     */
//    private Map<String, RedisCacheConfiguration> getCacheConfigurations() {
//        Map<String, RedisCacheConfiguration> configurationMap = new HashMap<>(16);
//        if (!CollectionUtils.isEmpty(cacheExpiredProperties.getCacheExpired())) {
//            for (Map.Entry<String, Long> entry : cacheExpiredProperties.getCacheExpired().entrySet()) {
//                //缓存键,且3600*10秒后过期,3600*10秒后再次调用方法时需要重新缓存
//                configurationMap.put(entry.getKey(), this.getDefaultCacheConfiguration(entry.getValue()));
//            }
//        }
//        configurationMap.put(CacheTimeEnum.M1, this.getDefaultCacheConfiguration(60));
//        configurationMap.put(CacheTimeEnum.M5, this.getDefaultCacheConfiguration(5 * 60));
//        configurationMap.put(CacheTimeEnum.M10, this.getDefaultCacheConfiguration(10 * 60));
//        configurationMap.put(CacheTimeEnum.M30, this.getDefaultCacheConfiguration(30 * 60));
//        configurationMap.put(CacheTimeEnum.H1, this.getDefaultCacheConfiguration(60 * 60));
//        configurationMap.put(CacheTimeEnum.H2, this.getDefaultCacheConfiguration(2 * 60 * 60));
//        configurationMap.put(CacheTimeEnum.H6, this.getDefaultCacheConfiguration(6 * 60 * 60));
//        configurationMap.put(CacheTimeEnum.H12, this.getDefaultCacheConfiguration(12 * 60 * 60));
//        configurationMap.put(CacheTimeEnum.D1, this.getDefaultCacheConfiguration(24 * 60 * 60));
//        configurationMap.put(CacheTimeEnum.D2, this.getDefaultCacheConfiguration(2 * 24 * 60 * 60));
//        configurationMap.put(CacheTimeEnum.D5, this.getDefaultCacheConfiguration(5 * 24 * 60 * 60));
//        configurationMap.put(CacheTimeEnum.D7, this.getDefaultCacheConfiguration(7 * 24 * 60 * 60));
//
//        return configurationMap;
//    }
//
//    /**
//     * 获取jcache的缓存配置(针对于键)
//     *
//     * @param seconds 键过期时间
//     * @return .
//     */
//    private RedisCacheConfiguration getDefaultCacheConfiguration(long seconds) {
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        om.addMixIn(Collection.class, HibernateCollectionMixIn.class);
//
//        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
//        jackson2JsonRedisSerializer.setObjectMapper(om);
//
//        return RedisCacheConfiguration
//                .defaultCacheConfig()
//                .disableCachingNullValues()
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
//                .entryTtl(Duration.ofSeconds(seconds));
//    }
//
//    /**
//     * @param jcacheConnectionFactory .
//     * @return .
//     */
//    @Bean(name = "jcacheTemplate")
//    public RedisTemplate<String, Object> jcacheTemplate(RedisConnectionFactory jcacheConnectionFactory) {
//        RedisTemplate<String, Object> jcacheTemplate = new RedisTemplate<>();
//        jcacheTemplate.setConnectionFactory(jcacheConnectionFactory);
//
//        jcacheTemplate.setKeySerializer(new StringRedisSerializer());
//        jcacheTemplate.setHashKeySerializer(new StringRedisSerializer());
//        jcacheTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//        jcacheTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
//
//        log.info("RedisTemplate loading success ");
//        return jcacheTemplate;
//    }
}
