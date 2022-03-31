package top.keiskeiframework.common.token.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 *
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/3/11 10:50
 */
@Component
@ConfigurationProperties(prefix = "keiskei.token")
@Data
public class TokenProperties {
    private String tokenHeader = "X-Auth-Token";

    private String tokenSecret;
}
