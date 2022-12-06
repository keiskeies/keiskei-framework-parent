package top.keiskeiframework.cache.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 不同键的缓存时间
 * </p>
 *
 * @author keiskei
 * @since 2022年12月5日 15:41:50
 */
@Data
@Component
@ConfigurationProperties(prefix = "keiskei.cache")
public class KeiskeiCacheProperties {


    /**
     * 不同键的缓存时间
     */
    private List<CacheTimer> timers;

    @Data
    public static class CacheTimer{
        /**
         * 键前缀
         */
        private String keySuffix;

        /**
         * 失效时间
         */
        private Long second;
    }
}
