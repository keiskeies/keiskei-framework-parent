package top.keiskeiframework.cloud.feign.config;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * feign配置文件
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/5/2 15:29
 */
@Configuration
public class FeignClientConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignResultDecoder();
    }
}
