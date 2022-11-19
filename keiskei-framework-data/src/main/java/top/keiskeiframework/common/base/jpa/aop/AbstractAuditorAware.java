package top.keiskeiframework.common.base.jpa.aop;

import lombok.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.util.StringUtils;
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
        String userId = MdcUtils.getUserId();
        if (!StringUtils.isEmpty(userId)) {
            return Optional.of(Long.valueOf(userId));
        }
        return Optional.empty();
    }
}
