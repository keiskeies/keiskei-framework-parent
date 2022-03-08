package top.keiskeiframework.cpreading.reader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author James Chen right_way@foxmail.com
 * @version 1.0
 * <p>
 * 启动类
 * </p>
 * @since 2020/11/22 22:26
 */
@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing
@EnableFeignClients
public class ReaderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReaderApplication.class, args);
    }
}