package top.keiskeiframework.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * 自动获取操作人员ID实现类
 * @author cjm
 */
@Configuration
public class SpringSecurityAuditorAware implements AuditorAware<String>{

    public static ThreadLocal<String> CREATE_USER = new ThreadLocal<>();

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(CREATE_USER.get());
    }

}


