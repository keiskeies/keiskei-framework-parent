package top.keiskeiframework.system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import top.keiskeiframework.common.base.service.impl.AbstractAuditorAware;
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

@Configuration("partnerAuditorAware")
public class PartnerAuditorAware extends AbstractAuditorAware implements AuditorAware<Long> {

    @Override
    @NonNull
    public Optional<Long> getCurrentAuditor() {
        try {
            return Optional.of(SecurityUtils.getSessionUser().getId());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}

