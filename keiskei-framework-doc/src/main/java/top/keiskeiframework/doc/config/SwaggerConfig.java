package top.keiskeiframework.doc.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
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
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;

/**
 * <p>
 * 接口文档相关配置
 * 接口文档地址：
 * http://127.0.0.1:10037/doc.html
 * </p>
 *
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
                        .version("v2")
                        .description("aminer管理平台接口文档")
                        .contact(new Contact("James Chen", "http://www.keisksi.top", "right_way@foxmail.com"))
                        .build())
                .securitySchemes(
                        Collections.singletonList(
                                new ApiKey("后台验证请求头", "Access-Token", "header")
                        )
                )
                .groupName("v2")
                .securityContexts(
                        Collections.singletonList(
                                SecurityContext.builder()
                                        .securityReferences(Collections.singletonList(new SecurityReference(
                                                "Access-Token",
                                                new AuthorizationScope[]{new AuthorizationScope("global", "accessEverything")})))
                                        .forPaths(PathSelectors.regex("^/admin.*$"))
                                        .build()
                        )

                )
                .directModelSubstitute(LocalDate.class, String.class)
                .directModelSubstitute(LocalTime.class, String.class)
                .directModelSubstitute(LocalDateTime.class, String.class);
    }

}
