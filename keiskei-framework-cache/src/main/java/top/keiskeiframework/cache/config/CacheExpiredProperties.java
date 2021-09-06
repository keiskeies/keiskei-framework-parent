package top.keiskeiframework.cache.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 跨域返回请求头信息
 *
 * @author 陈加敏
 * @since 2019/7/15 13:42
 */
@Component
@ConfigurationProperties(prefix = "keiskei")
@Data
public class CacheExpiredProperties {
    private Map<String, Long> cacheExpired = new HashMap<>();
}
