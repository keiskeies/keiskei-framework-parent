package top.keiskeiframework.common.config;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * <p>
 *
 * </P>
 *
 * @author CJM right_way@foxmail.com
 * @since 2021/11/21 18:23
 */
public class SpringMongoAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.empty();
    }
}
