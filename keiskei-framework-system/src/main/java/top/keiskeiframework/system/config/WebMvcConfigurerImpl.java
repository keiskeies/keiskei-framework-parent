package top.keiskeiframework.system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author James Chen right_way@foxmail.com
 * @since 2020/9/25 12:37 下午
 */


@Configuration
public class WebMvcConfigurerImpl implements WebMvcConfigurer {

    @Value("${keiskei.cross:false}")
    private Boolean cross;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RestApiInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.html");
    }

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        if (cross) {
            registry.addMapping("/**")
                    .allowedOrigins("*")
                    .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE", "PATCH")
                    .maxAge(3600)
                    .allowedHeaders("*")
                    .allowCredentials(true);
        }
    }
}