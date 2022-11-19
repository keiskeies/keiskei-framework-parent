package top.keiskeiframework.system.properties;

import lombok.Data;
import org.springframework.http.HttpMethod;

/**
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/21 14:12
 */
@Data
public class AuthenticateUrl {
    private String path;
    private HttpMethod method;
}
