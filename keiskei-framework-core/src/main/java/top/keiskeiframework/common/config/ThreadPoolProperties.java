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
    private Integer corePoolSize;
    private Integer maximumPoolSize;
    private Long keepAliveTime;
    private Integer capacity;
    private String threadNamePrefix;

    public ThreadPoolProperties() {
        this.corePoolSize = 200;
        this.maximumPoolSize = 400;
        this.keepAliveTime = 60L;
        this.capacity = 250;
        this.threadNamePrefix = "keiskei-thread-pool";
    }
}
