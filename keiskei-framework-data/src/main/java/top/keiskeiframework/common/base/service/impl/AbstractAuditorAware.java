package top.keiskeiframework.common.base.service.impl;

import lombok.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import top.keiskeiframework.common.util.MdcUtils;

import java.util.Optional;

/**
 * <p>
 * 创建人信息
 * </p>
 *
 * @author cjm
 */
@Configuration
public class AbstractAuditorAware implements AuditorAware<Long> {


    @Override
    @NonNull
    public Optional<Long> getCurrentAuditor() {
        return Optional.of(Long.valueOf(MdcUtils.getUserId()));
    }
}
