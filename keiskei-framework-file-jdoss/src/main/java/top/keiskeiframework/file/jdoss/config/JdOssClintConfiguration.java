package top.keiskeiframework.file.jdoss.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * <p>
 *
 * </p>
 *
 * @author ：陈加敏 right_way@foxmail.com
 * @since ：2019/11/5 22:34
 */
@Configuration
public class JdOssClintConfiguration {

    @Bean("longOssClint")
    public AmazonS3 amazonS3(FileJdOssProperties fileJdOssProperties) {

        String endpoint = fileJdOssProperties.getInternalEndpoint();
        if (StringUtils.isEmpty(endpoint)) {
            endpoint = fileJdOssProperties.getEndpoint();
        }
        ClientConfiguration config = new ClientConfiguration();

        AwsClientBuilder.EndpointConfiguration endpointConfig =
                new AwsClientBuilder.EndpointConfiguration(endpoint, fileJdOssProperties.getSigningRegion());

        AWSCredentials awsCredentials = new BasicAWSCredentials(fileJdOssProperties.getAccessKeyId(), fileJdOssProperties.getAccessKeySecret());
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);

        return AmazonS3Client.builder()
                .withEndpointConfiguration(endpointConfig)
                .withClientConfiguration(config)
                .withCredentials(awsCredentialsProvider)
                .disableChunkedEncoding()
                .withPathStyleAccessEnabled(true)
                .build();

    }
}
