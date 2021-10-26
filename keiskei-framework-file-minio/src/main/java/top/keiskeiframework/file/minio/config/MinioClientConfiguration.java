package top.keiskeiframework.file.minio.config;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 *
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/10/22 14:33
 */
@Configuration
public class MinioClientConfiguration {


    @Bean
    public MinioClient minioClient(FileMinioProperties fileMinioProperties) {
        return MinioClient.builder()
                .endpoint(fileMinioProperties.getUrl())
                .credentials(fileMinioProperties.getAccessKey(), fileMinioProperties.getSecretKey())
                .build();
    }

}
