package top.keiskeiframework;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

/**
 * @author cjm
 */
@SpringBootApplication
@MapperScan("top.keiskeiframework.**.mapper")
@EnableCaching
public class QmPerformanceApplication {
    public static void main(String[] args) {
        SpringApplication.run(QmPerformanceApplication.class, args);
    }

    @Bean
    public TomcatServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers(connector -> {
            connector.setProperty("relaxedQueryChars", "(),/:;<=>?@[\\]{}");
            connector.setProperty("rejectIllegalHeader", "false");
        });
        return factory;
    }
}
