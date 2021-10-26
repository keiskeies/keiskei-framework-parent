package top.keiskeiframework.file.minio.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/10/22 14:27
 */
@Data
@Component
@ConfigurationProperties(prefix = "keiskei.file.minio")
public class FileMinioProperties {

    private String url;
    private String accessKey;
    private String secretKey;
    private String bucket;

    @Bean
    public MinioClient minioClient() {

    }

}
