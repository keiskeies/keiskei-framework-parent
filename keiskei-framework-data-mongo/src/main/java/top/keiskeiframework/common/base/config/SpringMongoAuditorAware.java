package top.keiskeiframework.common.base.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import top.keiskeiframework.common.util.MdcUtils;

import java.util.Optional;

/**
 * <p>
 *
 * </P>
 *
 * @author CJM right_way@foxmail.com
 * @since 2021/11/21 18:23
 */
@Configuration
public class SpringMongoAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(MdcUtils.getUserId());
    }
}
