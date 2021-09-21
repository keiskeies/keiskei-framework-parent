package top.keiskeiframework.cache.simple.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
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

}
