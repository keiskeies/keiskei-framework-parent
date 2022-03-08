package top.keiskeiframework.cpreading.book;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
@EnableWebSecurity
@EnableScheduling
@EnableJpaAuditing
public class BookApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookApplication.class, args);
    }
}