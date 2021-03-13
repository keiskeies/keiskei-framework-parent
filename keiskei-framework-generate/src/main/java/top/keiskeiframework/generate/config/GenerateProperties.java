package top.keiskeiframework.generate.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author James Chen right_way@foxmail.com
 * @version 1.0
 * <p>
 * 文件生成配置
 * </p>
 * @since 2020/12/27 21:49
 */
@Data
@Component
@ConfigurationProperties(prefix = "keiskei-server-conf.generate")
public class GenerateProperties {

    private String baseServerPath;
    private String baseAdminPath;
    private String baseFrontPath;
}
