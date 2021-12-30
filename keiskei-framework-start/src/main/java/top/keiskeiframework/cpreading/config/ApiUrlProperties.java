package top.keiskeiframework.cpreading.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 前端请求路径
 *
 * @author James Chen right_way@foxmail.com
 * @since 2018年9月30日 下午5:11:56
 */
@Component
@Data
@ConfigurationProperties(prefix = "keiskei.api-url")
public class ApiUrlProperties {
    private List<String> permitUri;
}
