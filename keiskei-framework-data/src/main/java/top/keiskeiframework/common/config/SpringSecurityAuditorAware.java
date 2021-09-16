package top.keiskeiframework.common.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import java.util.Optional;

/**
 * 自动获取操作人员部门实现类
 *
 * @author cjm
 */
@Configuration
@EnableMongoAuditing
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    public static ThreadLocal<String> CREATE_USER = new ThreadLocal<>();

    @Override
    public Optional<String> getCurrentAuditor() {
        String p = CREATE_USER.get();
        CREATE_USER.remove();
        return Optional.ofNullable(p);
    }

}


