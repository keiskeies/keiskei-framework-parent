package top.keiskeiframework.cloud.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import top.keiskeiframework.common.util.JwtTokenUtils;

/**
 * <p>
 * token相关配置
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

    private String tokenSecret = "secret";

    private Integer expires = 7200;

    public void setTokenSecret(String tokenSecret) {
        JwtTokenUtils.SECRET = tokenSecret;
        this.tokenSecret = tokenSecret;
    }

    public void setExpires(Integer expires) {
        JwtTokenUtils.EXPIRES = expires;
        this.expires = expires;
    }
}
