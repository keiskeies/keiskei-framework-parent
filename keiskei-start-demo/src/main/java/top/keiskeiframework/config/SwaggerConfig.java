package top.keiskeiframework.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author James Chen right_way@foxmail.com
 * @since 2020/9/30 11:03 下午
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
@Profile({"dev"})
public class SwaggerConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public Docket createRestApi() {

        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage("top.keiskeiframework"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(new ApiInfoBuilder()
                        .title("接口文档")
                        .version("1.0.0")
                        .contact(new Contact("James Chen", "http://www.cjm.jx.cn", "right_way@foxmail.com"))
                        .build())
                .directModelSubstitute(LocalDate.class, String.class)
                .directModelSubstitute(ObjectId.class, String.class)
                .directModelSubstitute(LocalTime.class, String.class)
                .directModelSubstitute(LocalDateTime.class, String.class);
    }

}
