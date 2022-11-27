package top.keiskeiframework.cloud.feign.front.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import top.keiskeiframework.common.util.MdcUtils;

/**
 * <p>
 * feign 请求头向后传递
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2022/4/3 21:34
 */
@Slf4j
@Configuration
public class FeignRequestInterceptor implements RequestInterceptor {


    @Override
    public void apply(RequestTemplate template) {
        template.header(MdcUtils.USER_ID, MdcUtils.getUserId());
        template.header(MdcUtils.USER_NAME, MdcUtils.getUserName());
        template.header(MdcUtils.USER_DEPARTMENT, MdcUtils.getUserDepartment());
        template.header(MdcUtils.CHECK_DEPARTMENT, MdcUtils.getCheckDepartment());
    }
}


