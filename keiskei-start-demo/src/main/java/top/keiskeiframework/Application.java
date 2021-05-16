package top.keiskeiframework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * @author James Chen right_way@foxmail.com
 * @version 1.0
 * <p>
 * 启动类
 * </p>
 * @since 2020/11/22 22:26
 */
@SpringBootApplication
@EnableCaching
@EnableWebSecurity
@EnableScheduling
@EnableJpaAuditing
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
