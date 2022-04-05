package top.keiskeiframework.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 异步线程池配置参数
 * </p>
 *
 * @author v_chenjiamin
 * @since 2022/1/27 15:12
 */
@Data
@Component
@ConfigurationProperties(prefix = "keiskei.thread-pool")
public class ThreadPoolProperties {
    private Integer corePoolSize = 200;
    private Integer maximumPoolSize = 400;
    private Long keepAliveTime = 60L;
    private Integer capacity = 250;
    private String threadNamePrefix = "keiskei-thread-pool";
}
