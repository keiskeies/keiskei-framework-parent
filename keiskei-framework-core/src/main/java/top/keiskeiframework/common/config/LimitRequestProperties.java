package top.keiskeiframework.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 跨域返回请求头信息
 *
 * @author 陈加敏
 * @since 2019/7/15 13:42
 */
@Component
@ConfigurationProperties(prefix = "keiskei.limit-request")
@Data
public class LimitRequestProperties {
    private Integer minutes = 5;
    private Integer times = 5;
}
