package top.keiskeiframework.file.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.keiskeiframework.common.cache.service.CacheStorageService;
import top.keiskeiframework.file.service.impl.AbstractCacheStorageServiceImpl;

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
public class CacheStorageConfiguration {


    @Bean("cacheStorageServiceFile")
    public CacheStorageService cacheStorageService(CacheStorageService cacheStorageService) {
        if (null == cacheStorageService) {
            cacheStorageService = new AbstractCacheStorageServiceImpl(new ConcurrentHashMap<>(16));
        }
        return cacheStorageService;

    }
}
