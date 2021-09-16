package top.keiskeiframework.common.base.service.impl;

import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import top.keiskeiframework.common.util.SecurityUtils;

import java.util.Optional;

/**
 * @author cjm
 */
public abstract class AbstractAuditorAware implements AuditorAware<Long> {

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
