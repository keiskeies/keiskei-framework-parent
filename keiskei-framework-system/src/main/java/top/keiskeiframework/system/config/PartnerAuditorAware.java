package top.keiskeiframework.system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import top.keiskeiframework.common.util.SecurityUtils;


import java.util.Optional;

/**
 * <p>
 * 获取创建人部门标识
 * </p>
 *
 * @author James Chen right_way@foxmail.com
 * @since 2020/12/17 16:47
 */

@Configuration
public class PartnerAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            return Optional.of(SecurityUtils.getSessionUser().getDepartment());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}

