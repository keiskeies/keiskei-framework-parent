package top.keiskeiframework.doc.config;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Sets;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.ModelSpecification;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingScannerPlugin;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * <p>
 * 接口文档添加用户登陆文档
 * </p>
 *
 * @author v_chenjiamin
 * @since 2021/8/25 14:53
 */
@Component
@Profile({"dev"})
public class SwaggerAddition implements ApiListingScannerPlugin {

    /**
     * Implement this method to manually add ApiDescriptions
     * 实现此方法可手动添加ApiDescriptions
     *
     * @param context - Documentation context that can be used infer documentation context
     * @return List of {@link ApiDescription}
     * @see ApiDescription
     */
    @Override
    public List<ApiDescription> apply(DocumentationContext context) {
        return Arrays.asList(getSelfApiDescription(), getLoginApiDescription());
    }

    /**
     * 登陆相关接口
     *
     * @return 。
     */
    private ApiDescription getLoginApiDescription() {
        Operation loginOperation = new OperationBuilder(new CachingOperationNameGenerator())
                .method(HttpMethod.POST)
                .summary("用户名密码登录")
                .notes("username/password登录")
                // 接收参数格式
                .consumes(Sets.newHashSet(APPLICATION_FORM_URLENCODED_VALUE))
                // 返回参数格式
                .produces(Sets.newHashSet(APPLICATION_JSON_VALUE))
                .tags(Sets.newHashSet("登录"))
                .parameters(Arrays.asList(
                        new ParameterBuilder()
                                .description("用户名")
                                .type(new TypeResolver().resolve(String.class))
                                .name("username")
                                .defaultValue("superadmin")
                                .parameterType("query")
                                .parameterAccess("access")
                                .required(true)
                                .modelRef(new ModelRef("string"))
                                .build(),
                        new ParameterBuilder()
                                .description("密码")
                                .type(new TypeResolver().resolve(String.class))
                                .name("password")
                                .defaultValue("123456")
                                .parameterType("query")
                                .parameterAccess("access")
                                .required(true)
                                .modelRef(new ModelRef("string"))
                                .build()
                ))
                .responseMessages(Collections.singleton(
                        new ResponseMessageBuilder().code(200).message("请求成功")
                                .responseModel(new ModelRef("TokenUser")).build()))
                .build();

        return new ApiDescription("登录", "/system/login",
                "",
                "登录接口",
                Collections.singletonList(loginOperation), false);
    }

    /**
     * 用户中心相关接口
     *
     * @return 。
     */
    private ApiDescription getSelfApiDescription() {
        Operation detailOperation = new OperationBuilder(new CachingOperationNameGenerator())
                .method(HttpMethod.GET)
                .summary("详情")
                .notes("个人信息详情")
                .tags(Sets.newHashSet("系统设置-个人中心"))
                .produces(Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE))
                .responseMessages(Collections.singleton(
                        new ResponseMessageBuilder().code(200).message("请求成功")
                                .responseModel(new ModelRef("UserDto")).build()
                ))
                .build();
        Operation editOperation = new OperationBuilder(new CachingOperationNameGenerator())
                .method(HttpMethod.PUT)
                .summary("修改")
                .notes("修改个人信息")
                .tags(Sets.newHashSet("系统设置-个人中心"))
                .produces(Sets.newHashSet(APPLICATION_JSON_VALUE))
                .parameters(Collections.singletonList(
                        new ParameterBuilder()
                                .parameterType("body")
                                .modelRef(new ModelRef("UserDto"))
                                .description("用户信息")
                                .name("userDto")
                                .required(true)
                                .build()
                ))
                .responseMessages(Collections.singleton(
                        new ResponseMessageBuilder().code(200).message("请求成功")
                                .responseModel(new ModelRef("R")).build()
                ))
                .build();

        Operation editPasswordOperation = new OperationBuilder(new CachingOperationNameGenerator())
                .method(HttpMethod.PATCH)
                .summary("修改密码")
                .notes("修改个人密码")
                .tags(Sets.newHashSet("系统设置-个人中心"))
                .produces(Sets.newHashSet(APPLICATION_JSON_VALUE))
                .parameters(Collections.singletonList(
                        new ParameterBuilder()
                                .parameterType("body")
                                .modelRef(new ModelRef("UserPasswordDto"))
                                .description("用户密码信息")
                                .name("userPasswordDto")
                                .required(true)
                                .build()
                ))
                .responseMessages(Collections.singleton(
                        new ResponseMessageBuilder()
                                .code(200)
                                .message("请求成功")
                                .responseModel(new ModelRef("R<<Boolean>>"))
                                .build()
                ))
                .build();

        return new ApiDescription("系统设置-个人中心", "/system/self",
                ""
                ,
                "个人中心",
                Arrays.asList(editOperation, editPasswordOperation, detailOperation), false);
    }

    /**
     * 是否使用此插件
     *
     * @param documentationType swagger文档类型
     * @return true 启用
     */
    @Override
    public boolean supports(@NonNull DocumentationType documentationType) {
        return DocumentationType.SWAGGER_2.equals(documentationType);
    }
}
