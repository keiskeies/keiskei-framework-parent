package top.keiskeiframework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * @author ${author}
 * @version 1.0
 * <p>
 * ${comment} 主启动类
 * </p>
 * @since ${since}
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
